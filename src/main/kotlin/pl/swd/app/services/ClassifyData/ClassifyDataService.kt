package pl.swd.app.services.ClassifyData

import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix
import org.apache.commons.math3.stat.correlation.Covariance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.swd.app.models.ClassifySelectedDataModel
import pl.swd.app.models.DataRow
import pl.swd.app.models.DataValue
import pl.swd.app.services.ConvertValueService
import pl.swd.app.services.ProjectService
import pl.swd.app.views.TabsView
import pl.swd.app.views.modals.ClassifyDataModal
import pl.swd.app.views.modals.ConvertValuesModal
import tornadofx.find
import java.lang.Exception
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.HashMap

@Service
class ClassifyDataService {

    @Autowired private lateinit var projectService: ProjectService
    @Autowired private lateinit var convertValueService: ConvertValueService

    fun showDialog(tabsView: TabsView) {
        val selectedTabIndex = tabsView.root.selectionModel.selectedIndex

        if (selectedTabIndex != -1 && projectService.currentProject.value.isPresent) {
            val columnList = generateColumnList(selectedTabIndex)

            if (columnList.isEmpty()) return

            val view = find<ClassifyDataModal>(params = mapOf(ConvertValuesModal::columnNameList to columnList)).apply {
                openModal(block = true)
            }

            if (view.status.isCompleted()) {
                clasifiData(view.getClassifySelectedData(), selectedTabIndex)
            }
        }
    }

    private fun generateColumnList(tabIndex: Int): ArrayList<String> {
        checkAndConvertValues(tabIndex)

        var columnNameList = ArrayList<String>()
        val project = projectService.currentProject.value?.let { it } ?: return columnNameList
        val rowValuesMap = project.get().spreadSheetList[tabIndex].dataTable.rows.first().rowValuesMap

        for(entry in rowValuesMap) {
            val columnValue = entry.value.value as String

            if (columnValue.toDoubleOrNull() != null) {
                columnNameList.add(entry.key)
            }
        }

        return columnNameList
    }

    private fun checkAndConvertValues(tabIndex: Int) {
        val project = projectService.currentProject.value?.let { it } ?: return
        val rowValuesMap = project.get().spreadSheetList[tabIndex].dataTable.rows.first().rowValuesMap

        for(entry in rowValuesMap) {
            if (rowValuesMap.containsKey("${entry.key}_convert")) continue

            val columnValue = entry.value.value as String

            if (columnValue.toDoubleOrNull() == null) convertValueService.convertSelectedColumn(entry.key, tabIndex)
        }
    }

    private fun clasifiData(userSelectedParameters: ClassifySelectedDataModel, tabIndex: Int) {
        val project = projectService.currentProject.value?.let { it } ?: return
        val spreadSheet = projectService.currentProject.value.get().spreadSheetList[tabIndex]
        val rows = project.get().spreadSheetList[tabIndex].dataTable.rows.toList()
        val columns = project.get().spreadSheetList[tabIndex].dataTable.columns.toList()

        if (userSelectedParameters.newDataRow.rowValuesMap.keys.size != rows.first().rowValuesMap.keys.size) {
            val convertColumns = userSelectedParameters.newDataRow.rowValuesMap.keys.filter { it.contains("_convert") }

            convertColumns.forEach { col ->
                val existRow = rows.find { it.rowValuesMap.getValue(col).value.toString() ==  userSelectedParameters.newDataRow.rowValuesMap.getValue(col).value.toString() }.let { it } ?: return
                val nonConvertColumnName = col.replace("_convert", "")
                val nonConvertValue = existRow.rowValuesMap.getValue(nonConvertColumnName)

                userSelectedParameters.newDataRow.addValue(nonConvertColumnName, nonConvertValue)
            }
        }

        val newValue = classify(tabIndex, userSelectedParameters)

        userSelectedParameters.newDataRow.addValue(userSelectedParameters.decisionClassCol, DataValue(newValue))
        spreadSheet.dataTable.addRow(userSelectedParameters.newDataRow)
    }

    private fun classify(tabIndex: Int, conf: ClassifySelectedDataModel): String {
        val project = projectService.currentProject.value?.let { it } ?: return ""
        val rows = project.get().spreadSheetList[tabIndex].dataTable.rows.toList()
        var distancesMap: HashMap<DataRow, Double> = hashMapOf()

        if (conf.metric == ClassifiDistanceMetric.MAHALANOBIS) {
            val invCov = inverseCovarianceMatrix(rows, conf.decisionCols)
            invCov?.let {
                rows.forEachIndexed { i, d ->
                    val dist = calculateDistance(conf.metric, conf.newDataRow, d, conf.decisionCols, invCov)
                    distancesMap.set(d, dist)
                }
            }
        } else {
            rows.forEachIndexed { i, d ->
                val dist = calculateDistance(conf.metric, conf.newDataRow, d, conf.decisionCols, null)
                distancesMap.set(d, dist)
            }
        }

        val sortedDistances = distancesMap.toList().sortedBy { it.second }

        val frequencies = hashMapOf<String, Int>()

        var k = conf.kNum
        if (k % 2 == 0) {
            k += + 1
        }

        for (i in 0..k) {
            val pair = sortedDistances[i]
            val classifier = pair.first.rowValuesMap.getValue(conf.decisionClassCol).value.toString()  //.value(conf.decisionClassCol.name).toString()

            var count = frequencies.get(classifier) ?: 0

            frequencies.put(classifier, count + 1)
        }

        val sortedFrequencies = frequencies.toList().sortedBy { it.second }

        val finalClass = sortedFrequencies.last().first

        return finalClass

//        return Observable.create<DataRow> { s ->
//            conf.newDataRow.set(finalClass ?: "unknown", conf.decisionClassCol.name)
//            val newRow = DataRow.createNew(conf.newDataRow)
//            s.onNext(newRow)
//            s.onCompleted()
//        }
    }

    private fun inverseCovarianceMatrix(data: List<DataRow>, columns: List<String>): RealMatrix? {
        var matrixArray = emptyArray<DoubleArray>()

        data.forEach {
            var currentData = it
            var matrixRow = DoubleArray(0)
            columns.forEach {
                var existingValue = currentData.rowValuesMap.getValue(it).value.toString().toDouble()
                matrixRow = matrixRow.plus(existingValue)
            }
            matrixArray = matrixArray.plus(matrixRow)
        }

        val mx = MatrixUtils.createRealMatrix(matrixArray)
        val cov = Covariance(mx).covarianceMatrix
        try {
            val invCov = MatrixUtils.inverse(cov)
            return invCov
        }catch (e: Exception) {

        }

        return null
    }

    private fun calculateDistance(metric: ClassifiDistanceMetric, newRow: DataRow, existingRow: DataRow, columns: List<String>, invCov: RealMatrix?): Double {
        var distance = 0.0

        if (metric == ClassifiDistanceMetric.EUKLIDES) {

            var sum = 0.0
            columns.forEach {
                var existingValue = existingRow.rowValuesMap.getValue(it).value.toString().toDouble()
                var newValue = newRow.rowValuesMap.getValue(it).value.toString().toDouble()
                sum += Math.pow(existingValue - newValue, 2.0)
            }
            distance = Math.sqrt(sum)

        } else if (metric == ClassifiDistanceMetric.MANHATTAN) {

            var sum = 0.0
            columns.forEach {
                var existingValue = existingRow.rowValuesMap.getValue(it).value.toString().toDouble()
                var newValue = newRow.rowValuesMap.getValue(it).value.toString().toDouble()
                sum += Math.abs(existingValue - newValue)
            }
            distance = sum

        } else if (metric == ClassifiDistanceMetric.INFINITY) {
            var max = 0.0
            columns.forEach {
                var existingValue = existingRow.rowValuesMap.getValue(it).value.toString().toDouble()
                var newValue = newRow.rowValuesMap.getValue(it).value.toString().toDouble()
                val value = Math.abs(existingValue - newValue)
                if (value > max) {
                    max = value
                }
            }
            distance = max
        } else if (metric == ClassifiDistanceMetric.MAHALANOBIS) {

            var matrixArray = emptyArray<DoubleArray>()

            columns.forEach {
                var existingValue = existingRow.rowValuesMap.getValue(it).value.toString().toDouble()
                var newValue = newRow.rowValuesMap.getValue(it).value.toString().toDouble()
                var matrixRow = DoubleArray(0)
                matrixRow = matrixRow.plus(existingValue - newValue)
                matrixArray = matrixArray.plus(matrixRow)
            }
            val mx = MatrixUtils.createRealMatrix(matrixArray)
            val mxT = mx.transpose()


            val multiplyResult = mxT.multiply(invCov).multiply(mx)

            val multiplyValue = multiplyResult.getEntry(0,0)

            distance = Math.sqrt(multiplyValue)
        }

        return distance
    }
}
package pl.swd.app.services.ClassifyData

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.swd.app.models.ClassifySelectedDataModel
import pl.swd.app.models.DataValue
import pl.swd.app.services.ConvertValueService
import pl.swd.app.services.ProjectService
import pl.swd.app.views.TabsView
import pl.swd.app.views.modals.ClassifyDataModal
import pl.swd.app.views.modals.ConvertValuesModal
import tornadofx.find

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
                clasifiData(view.getClassifySelectedData())
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

    private fun clasifiData(userSelectedParameters: ClassifySelectedDataModel) {

    }
}
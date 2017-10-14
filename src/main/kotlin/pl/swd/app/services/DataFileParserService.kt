package pl.swd.app.services;

import org.springframework.stereotype.Service
import pl.swd.app.exceptions.FileParserException
import pl.swd.app.models.DataColumn
import pl.swd.app.models.DataRow
import pl.swd.app.models.DataTable
import pl.swd.app.models.DataValue
import tornadofx.*
import java.io.File
import io.reactivex.Observable

@Service
class DataFileParserService {
    fun generateDataTable(pair: Pair<File, List<String>>): DataTable {
        return parseFile(pair)
    }

    fun generateMockDataTable(): DataTable {
        val janId = DataValue("1")
        val janName = DataValue("Jan")
        val piotrId = DataValue("2")
        val piotrName = DataValue("Piotr")

        val idColumn = DataColumn("Id", arrayListOf(janId, piotrId))
        val nameColumn = DataColumn("Name", arrayListOf(janName, piotrName))

        val janRow = DataRow("1;Jan", mapOf("Id" to janId, "Name" to janName))
        val piotrRow = DataRow("2;Piotr", mapOf("Id" to piotrId, "Name" to piotrName))

        return DataTable(rows = arrayListOf(janRow, piotrRow).observable(),
                columns = arrayListOf(idColumn, nameColumn).observable())
    }

    private fun parseFile(pair: Pair<File, List<String>>): DataTable {
        val inputStream = pair.first.inputStream()
        var lineList = mutableListOf<String>()
        val separatorRegex = Regex("[\\s;]")
        var colums = mutableListOf<DataColumn>()
        var rows = mutableListOf<DataRow>()

        //Read lines file
        inputStream.bufferedReader().useLines { lines -> lines.forEach { lineList.add(it) } }
        //Remove coomented lines
        lineList = lineList.filter { (!it.startsWith("#") && !it.isEmpty()) }.toMutableList()


        if (pair.second.isEmpty()) {
            //Split columns name
            colums = lineList.first().split(separatorRegex).map { DataColumn(it, ArrayList()) }.toMutableList()
            lineList.removeAt(0)
        } else {
            val values = lineList.first().split(separatorRegex)
            colums = pair.second.map { DataColumn(it, ArrayList()) }.toMutableList()

            if (colums.size > values.size) {
                colums = colums.subList(0,values.size)
            } else if (colums.size < values.size){
                for (i in values.indices.minus(colums.size)) {
                    colums.add(DataColumn(i.toString(), ArrayList()))
                }
            }
        }

        //parse row data
        lineList.forEach {
            //Split row
            val values = it.split(separatorRegex)
            var rowMap = HashMap<String, DataValue>()

            if (values.size != colums.size) {
                throw FileParserException("The row contains the wrong number of data")
            }

            //Adding values to respective objects
            for (i in values.indices) {
                colums[i].columnValuesList.add(DataValue(values[i]))
                rowMap.put(colums[i].name, DataValue(values[i]))
            }

            rows.add(DataRow(it, rowMap))
        }

        return DataTable(rows = rows.observable(), columns = colums.observable())
    }
}



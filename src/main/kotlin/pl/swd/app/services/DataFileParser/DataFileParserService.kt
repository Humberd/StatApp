package pl.swd.app.services.DataFileParser;

import org.springframework.stereotype.Service
import pl.swd.app.exceptions.FileParserException
import pl.swd.app.models.DataColumn
import pl.swd.app.models.DataRow
import pl.swd.app.models.DataTable
import pl.swd.app.models.DataValue
import tornadofx.*

@Service
class DataFileParserService {
    /**
     * Takes a list of rows, list of columnNames and generates datatable out of it
     */
    fun generateDataTable(rows: List<String>, columnNames: List<String>, options: DataFileOption): DataTable {
        return parseFile(rows, columnNames, options)
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

    private fun parseFile(initialRows: List<String>, columnNames: List<String>, option: DataFileOption): DataTable {
        var lineList = initialRows
        val separatorRegex = Regex("[\\s;]")
        var colums: MutableList<DataColumn>
        val rows = mutableListOf<DataRow>()

        //Remove coomented lines
        lineList = lineList.filter { (!it.startsWith("#") && !it.isEmpty()) }.toMutableList()


        if (option.isAutoDetect()) {
            //Split columns name
            colums = lineList.first().split(separatorRegex).map { DataColumn(it, ArrayList()) }.toMutableList()
            lineList.removeAt(0)
        } else {
            val values = lineList.first().split(separatorRegex)
            colums = columnNames.map { DataColumn(it, ArrayList()) }.toMutableList()

            if (colums.isEmpty()) {
                for (i in values.indices) {
                    colums.add(DataColumn("Column " + i.toString(), ArrayList()))
                }
            } else if (colums.size > values.size) {
                colums = colums.subList(0, values.size)
            } else if (colums.size < values.size) {
                for (i in values.indices.minus(colums.indices)) {
                    colums.add(DataColumn("Column " + i.toString(), ArrayList()))
                }
            }
        }

        //parse row data
        lineList.forEach {
            //Split row
            val values = it.split(separatorRegex)
            val rowMap = HashMap<String, DataValue>()

            if (values.size != colums.size) {
                throw FileParserException("The row contains the wrong number of data")
            }

            //Adding values to respective objects
            for (i in values.indices) {
                val value = values[i].replace(",", ".")
                colums[i].columnValuesList.add(DataValue(value))
                rowMap.put(colums[i].name, DataValue(value))
            }

            rows.add(DataRow(it, rowMap))
        }

        return DataTable(rows = rows.observable(), columns = colums.observable())
    }
}



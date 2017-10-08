package pl.swd.app.controllers;

import pl.swd.app.models.DataColumn
import pl.swd.app.models.DataRow
import pl.swd.app.models.DataTable
import pl.swd.app.models.DataValue
import tornadofx.*
import java.io.File

class FileParserController : Controller() {
    fun generateDataTable(file: File): DataTable {
        return generateMockDataTable()
    }

    fun generateMockDataTable(): DataTable {
        val janId = DataValue("1", "Id")
        val janName = DataValue("Jan", "Name")
        val piotrId = DataValue("2", "Id")
        val piotrName = DataValue("Piotr", "Name")

        val idColumn = DataColumn("Id", arrayListOf(janId, piotrId))
        val nameColumn = DataColumn("Name", arrayListOf(janName, piotrName))

        val janRow = DataRow("1;Jan", arrayListOf(janId, janName))
        val piotrRow = DataRow("2;Piotr", arrayListOf(piotrId, piotrName))

        return DataTable(rows = arrayListOf(janRow, piotrRow),
                columns = arrayListOf(idColumn, nameColumn))
    }
}
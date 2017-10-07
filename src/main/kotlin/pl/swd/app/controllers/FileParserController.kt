package pl.swd.app.controllers;

import pl.swd.app.models.DataTable
import tornadofx.*
import java.io.File

class FileParserController: Controller() {
    fun generateDataTable(file: File): DataTable {
        return DataTable()
    }
}
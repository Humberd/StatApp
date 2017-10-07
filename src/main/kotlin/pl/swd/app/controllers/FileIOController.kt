package pl.swd.app.controllers

import io.reactivex.Observable
import javafx.stage.FileChooser
import tornadofx.*
import java.io.File

class FileIOController : Controller() {
    val openFileExtensions = arrayOf(FileChooser.ExtensionFilter("Text file", "*.txt"))

    fun openFileDialog(): List<File> {
        return chooseFile(title = "Open file", filters = openFileExtensions) {
            initialDirectory = File(getCurrentDirectory())
        }
    }

    fun getCurrentDirectory() = System.getProperty("user.dir")
}
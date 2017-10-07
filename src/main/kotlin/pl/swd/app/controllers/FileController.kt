package pl.swd.app.controllers

import javafx.stage.FileChooser
import tornadofx.*

class FileController: Controller() {
    val openFileExtensions = arrayOf(FileChooser.ExtensionFilter("Text file", "*.txt"))

    fun openFileDialog() {
        chooseFile(title = "Open file", filters = openFileExtensions)
    }
}
package pl.swd.app.services

import io.reactivex.Observable
import io.reactivex.rxkotlin.toObservable
import javafx.stage.FileChooser
import mu.KLogging
import org.springframework.stereotype.Service
import tornadofx.*
import java.io.File

@Service
class FileIOService {
    companion object : KLogging()

    val openFileExtensions = arrayOf(FileChooser.ExtensionFilter("Text file", "*.txt"))

    fun openFileDialog(): Observable<File> {
        return chooseFile(title = "Open file", filters = openFileExtensions) {
            initialDirectory = File(getCurrentDirectory())
        }.toObservable()
                .doOnNext { logger.debug { "Selected file: ${it.name}" } }
    }

    fun getCurrentDirectory() = System.getProperty("user.dir")
}
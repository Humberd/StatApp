package pl.swd.app.services

import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.rxkotlin.toObservable
import javafx.stage.FileChooser
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tornadofx.chooseFile
import java.io.File
import java.io.FileNotFoundException

@Service
class FileIOService {
    companion object : KLogging()

    @Autowired lateinit var gson: Gson

    val openFileExtensions = arrayOf(FileChooser.ExtensionFilter("Text file", "*.txt"))

    /**
     * Opens a new window with an option of choosing a file
     */
    fun openFileDialog(title: String = "Open File",
                       fileExtensions: Array<FileChooser.ExtensionFilter> = openFileExtensions,
                       initialDirectory: String = getCurrentDirectory()): Observable<File> {
        return chooseFile(title = title, filters = fileExtensions) {
            this.initialDirectory = File(initialDirectory)
        }.toObservable()
                .doOnNext { logger.debug { "Selected file: ${it.name}" } }
    }

    // todo finish this
    fun chooseFileDialog(title: String = "Choose File"): Observable<File> {
        return chooseFile(title, filters = emptyArray()) {
            initialDirectory = File(getCurrentDirectory())

        }.toObservable()
    }

    /**
     * Saves an object in JSON format to a file
     * If a file exists it overrides its content
     */
    fun <T> saveAsJsonToFile(data: T, fileName: String) {
        val jsonData = gson.toJson(data)

        File(fileName).apply {
            writeText(jsonData)
        }
    }

    /**
     * Gets an object from a file with JSON format
     */
    inline fun <reified T> getAsObjectFromJsonFile(fileName: String): T {
        val file = File(fileName).apply {
            if (!exists()) {
                throw FileNotFoundException("File $fileName does not exist")
            }

            if (isDirectory()) {
                throw FileNotFoundException("File $fileName is a directory")
            }
        }

        val jsonData = file.readText()

        return gson.fromJson(jsonData, T::class.java)
    }

    /**
     * Gets a current directory
     */
    fun getCurrentDirectory() = System.getProperty("user.dir")
}
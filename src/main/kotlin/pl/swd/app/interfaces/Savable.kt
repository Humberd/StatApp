package pl.swd.app.interfaces

/**
 * Indicates that implementing class can be saved or loaded from a file
 */
interface Savable {
    fun saveToFile()
    fun loadFromFile()
}
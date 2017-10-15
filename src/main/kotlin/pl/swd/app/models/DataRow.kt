package pl.swd.app.models;

data class DataRow(
        val rawInitialString: String,
        var rowValuesMap: Map<String, DataValue>
) {
    fun addValue(columnName: String, data: DataValue) {
        val rowValuesMapp = rowValuesMap.toMutableMap()
        rowValuesMapp.put(columnName, data)
        rowValuesMap = rowValuesMapp
    }
}
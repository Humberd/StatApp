package pl.swd.app.models;

data class DataRow(
        val rawInitialString: String,
        val rowValuesMap: Map<String, DataValue>
)
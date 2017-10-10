package pl.swd.app.models;

data class DataRow(
        val rawInitialString: String,
        var rowValuesMap: Map<String, DataValue>
)
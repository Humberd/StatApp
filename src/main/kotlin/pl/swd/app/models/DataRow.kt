package pl.swd.app.models;

data class DataRow(
        val rawInitialString: String,
        val rowValuesList: ArrayList<DataValue<Any>>
)
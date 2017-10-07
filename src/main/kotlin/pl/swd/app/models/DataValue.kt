package pl.swd.app.models;

data class DataValue<T>(
        val value: T,
        val columnName: String
)
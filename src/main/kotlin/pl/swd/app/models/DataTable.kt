package pl.swd.app.models;

data class DataTable (
        val rows: ArrayList<DataRow> = ArrayList(),
        val columns: ArrayList<DataColumn> = ArrayList()
)
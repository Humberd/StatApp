package pl.swd.app.models;

import javafx.collections.ObservableList

data class DataTable (
        val rows: ObservableList<DataRow>,
        val columns: ObservableList<DataColumn>
)
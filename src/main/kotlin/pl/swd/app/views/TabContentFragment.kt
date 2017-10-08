package pl.swd.app.views;

import com.github.thomasnield.rxkotlinfx.observeOnFx
import com.github.thomasnield.rxkotlinfx.toObservable
import io.reactivex.Observable
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableValue
import javafx.collections.ListChangeListener
import javafx.scene.control.TableView
import mu.KLogging
import pl.swd.app.exceptions.InvalidColumnException
import pl.swd.app.models.DataColumn
import pl.swd.app.models.DataRow
import pl.swd.app.models.DataTable
import pl.swd.app.models.DataValue
import tornadofx.*
import java.util.concurrent.TimeUnit

class TabContentFragment : Fragment("My View") {
    companion object : KLogging()

    val dataTable: DataTable by param()
    var table: TableView<DataRow> by singleAssign()

    override val root = borderpane {
        center { table = tableview() }
    }

    init {
        dataTable.columns.forEach(this::addColumn)
        table.items = dataTable.rows
        logger.debug { "Table created with ${dataTable.rows.size} rows" }
    }

    override fun onDock() {
        dataTable.columns.addListener { event: ListChangeListener.Change<out DataColumn>? ->
            event?.next()
            if (event?.wasAdded()!!) {
                event.addedSubList.forEach(this::addColumn)
            }

            if (event.wasRemoved()) {
                event.removed?.forEach(this::removeColumn)
            }
        }

        dataTable.rows.addListener { event: ListChangeListener.Change<out DataRow>? ->
            event?.next()
            if (event?.wasAdded()!!) {
                logger.debug { "Added ${event.addedSize} rows. Total size: ${event.list.size}" }
            }

            if (event.wasRemoved()) {
                logger.debug { "Removed ${event.removedSize} rows. Total size: ${event.list.size}" }
            }
        }
    }

    private fun addColumn(dataColumn: DataColumn) {
        logger.debug { "Added new column: '${dataColumn.name}'" }

        table.column(dataColumn.name, String::class)
                .setCellValueFactory { cellData ->
                    val dataValue = cellData?.value?.rowValuesMap?.get(dataColumn.name)

                    if (dataValue === null) {
                        throw InvalidColumnException("There are is no mapped value to clumnName: '${dataColumn.name}' in row: ${cellData.value.rowValuesMap}")
                    }

                    observable(dataValue, DataValue::value)
                }
    }

    private fun removeColumn(dataColumn: DataColumn) {
        TODO("Remove Column is not implemented yet")
    }
}
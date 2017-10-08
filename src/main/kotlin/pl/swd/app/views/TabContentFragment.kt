package pl.swd.app.views;

import javafx.collections.ListChangeListener
import javafx.scene.control.TableView
import mu.KLogging
import pl.swd.app.exceptions.InvalidColumnException
import pl.swd.app.models.DataColumn
import pl.swd.app.models.DataRow
import pl.swd.app.models.DataTable
import pl.swd.app.models.DataValue
import tornadofx.*

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

    /**
     * Adding  listeners
     */
    override fun onDock() {
        // todo Add removeListener on onDestroy?/unDock?
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

    /**
     * Adds a column to a table based on a dataColumn generated from source file
     */
    private fun addColumn(dataColumn: DataColumn) {
        logger.debug { "Added new column: '${dataColumn.name}'" }

        table.column(dataColumn.name, Any::class)
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
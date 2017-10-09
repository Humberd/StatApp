package pl.swd.app.views;

import javafx.scene.control.TabPane
import mu.KLogging
import pl.swd.app.models.SpreadSheet
import tornadofx.*

class TabsView : View("My View") {
    companion object: KLogging()

    override val root = TabPane()

    fun addTab(spreadSheet: SpreadSheet) {
        val tabWrapper = TabWrapper(spreadSheet)
        root.tabs.add(tabWrapper)
        /*Select a newly added tabWrapper*/
        root.selectionModel.select(tabWrapper)

        /*When clicking a "Rename" in a context menu it opens a RenameModal
        * And passes a tabInput to it*/
        tabWrapper.renameMenuItem.setOnAction {
            find(RenameTabModal::class, mapOf(RenameTabModal::tabInput to tabWrapper))
                    .openModal()
        }
    }
}

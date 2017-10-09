package pl.swd.app.views;

import javafx.scene.control.TabPane
import tornadofx.*

class TabsView : View("My View") {
    override val root = TabPane()

    fun addTab(tab: Tab) {
        root.tabs.add(tab)
        /*Need to select a newly added tab*/
        root.selectionModel.select(tab)

        /*When clicking a "Rename" in a context menu it opens a RenameModal
        * And passes a tabInput to it*/
        tab.renameMenuItem.setOnAction {
            find(RenameTabModal::class, mapOf(RenameTabModal::tabInput to tab))
                    .openModal()
        }
    }
}

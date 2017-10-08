package pl.swd.app.views;

import javafx.scene.control.TabPane
import tornadofx.*

class TabsView : View("My View") {
    override val root = TabPane()

    fun addTab(tabItem: TabItem) {
        root.tabs.add(tabItem)
        /*Need to select a newly added tab*/
        root.selectionModel.select(tabItem)

        /*When clicking a "Rename" in a context menu it opens a RenameModal
        * And passes a tabInput to it*/
        tabItem.renameMenuItem.setOnAction {
            find(RenameTabModal::class, mapOf(RenameTabModal::tabInput to tabItem))
                    .openModal()
        }
    }
}

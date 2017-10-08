package pl.swd.app.views;

import javafx.scene.control.TabPane
import tornadofx.*

class AppTabs : View("My View") {
    override val root = TabPane()

    fun addTab(appViewTab: AppViewTab) {
        root.tabs.add(appViewTab)
        root.selectionModel.select(appViewTab)

        /*When clicking a "Rename" in a context menu it opens a RenameModal
        * And passes a tabInput to it*/
        appViewTab.renameMenuItem.setOnAction {
            find(RenameTabView::class, mapOf(RenameTabView::tabInput to appViewTab)).openModal()
        }
    }
}

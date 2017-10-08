package pl.swd.app.views;

import tornadofx.*

class AppTabs : View("My View") {

    override val root = tabpane {

    }

    fun addTab(appViewTab: AppViewTab) {
        root.tabs.add(appViewTab)
        root.selectionModel.select(appViewTab)

        /*When clicking a "Rename" context menu it opens a RenameModal
        * And passes a tab to it*/
        appViewTab.renameMenuItem.setOnAction {
            find(RenameTabView::class, mapOf(RenameTabView::tab to appViewTab)).openModal()
        }
    }
}

package pl.swd.app.views;

import tornadofx.*

class AppTabs : View("My View") {

    override val root = tabpane {

    }

    fun addTab(appViewTab: AppViewTab) {
        root.tabs.add(appViewTab)
        root.selectionModel.select(appViewTab)

        appViewTab.renameMenuItem.setOnAction { find(RenameTabView::class).openModal() }
    }
}

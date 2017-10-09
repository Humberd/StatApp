package pl.swd.app.views;

import mu.KLogging
import pl.swd.app.models.SpreadSheet
import pl.swd.app.services.ProjectService
import pl.swd.app.utils.emptyObservableList
import tornadofx.*

class LeftDrawer : View("Drawer") {
    companion object: KLogging()

    val projectService: ProjectService by di()

    override val root = drawer {
        item("Project") {
            listview(emptyObservableList<SpreadSheet>()) {
                // todo add unsubscribe
                /*Subscribes to a current project and binds its spreadsheetsList to listview items*/
                projectService.currentProject
                        .map { currentProject ->
                            if (!currentProject.isPresent) {
                                logger.debug { "No Current Project - setting empty SpreadSheets list" }
                                return@map emptyObservableList<SpreadSheet>()
                            }

                            logger.debug { "Found New Project '${currentProject.get().name}' - binding SpreadSheets list" }
                            return@map currentProject.get().spreadSheetList
                        }.subscribe { items = it }

                /*Makes sure that whenever a spreadsheet name changes
                * it will properly reflect it in a listview*/
                cellFormat { spreadSheet ->
                    graphic = label(spreadSheet.nameProperty)
                }
            }
        }
    }
}
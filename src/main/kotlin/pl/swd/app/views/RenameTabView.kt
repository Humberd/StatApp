package pl.swd.app.views;

import tornadofx.*

class RenameTabView : View("Rename tab") {
    val foobar = Foobar("FOOBAR111")
    val model: RenameTabModel = RenameTabModel(foobar)

    override val root = form {
        fieldset("Tab Info") {
            field("Tab Name") {
                textfield(model.tabName)
            }
        }
        button("Commit") {
            action { println("Wrote to database!") }
        }

    }
    private fun save() {
        // Flush changes from the text fields into the model
        model.commit()

        // The edited person is contained in the model
        val person = model.person

        // A real application would persist the person here
        println("Saving ${person.name} / ${person.title}")
    }
}
// todo https://github.com/edvin/tornadofx/wiki/ViewModel
class RenameTabModel(foobar: Foobar): ItemViewModel<Foobar> {
    var tabName by property(foobar.tabName)
    fun tabNameProperty() = getProperty(RenameTabModel::tabName)
}

data class Foobar(
        val tabName: String = ""
)

class FoobarModel : ItemViewModel<Foobar>() {
    val tabName = bind(Foobar::tabName)
}





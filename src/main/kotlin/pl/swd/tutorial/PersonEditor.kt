package pl.swd.tutorial

import javafx.beans.property.SimpleStringProperty
import javafx.scene.layout.BorderPane
import tornadofx.*


class PersonEditor : View("Person Editor") {
    override val root = BorderPane()
    val persons = listOf(Person2("John", "Manager"), Person2("Jay", "Worker bee")).observable()
    val model = PersonModel(Person2())

    init {
        with(root) {
            center {
                tableview(persons) {
                    column("Name", Person2::nameProperty)
                    column("Title", Person2::titleProperty)

                    // Update the person inside the view model on selection change
                    model.rebindOnChange(this) { selectedPerson ->
                        println("foo")
                        person = selectedPerson ?: Person2()
                    }
                }
            }

            right {
                form {
                    fieldset("Edit person") {
                        field("Name") {
                            textfield(model.name).required()
                        }
                        field("Title") {
                            textfield(model.title)
                        }
                        button("Save") {
                            enableWhen(model.dirty)
                            action {
                                save()
                            }
                        }
                        button("Reset").action {
                            model.rollback()
                        }
                    }
                }
            }
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
class PersonModel(var person: Person2) : ViewModel() {
    val name = bind { person.nameProperty }
    val title = bind { person.titleProperty }
}

class Person2(name: String? = null, title: String? = null) {
    val nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty

    val titleProperty = SimpleStringProperty(this, "title", title)
    var title by titleProperty
}
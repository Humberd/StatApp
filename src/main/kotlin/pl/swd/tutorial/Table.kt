package pl.swd.tutorial

import com.github.thomasnield.rxkotlinfx.observeOnFx
import com.github.thomasnield.rxkotlinfx.subscribeOnFx
import io.reactivex.Observable
import javafx.scene.Parent
import tornadofx.*
import java.time.LocalDate
import java.time.Period
import java.util.concurrent.TimeUnit

private val persons = listOf(
        Person(1,"Samantha Stuart",LocalDate.of(1981,12,4)),
        Person(2,"Tom Marks",LocalDate.of(2001,1,23)),
        Person(3,"Stuart Gills",LocalDate.of(1989,5,23)),
        Person(3,"Nicole Williams",LocalDate.of(1998,8,11))
).observable()


class Table: View() {
    override val root = vbox {
        tableview(persons) {
            column("ID",Person::id)
            column("Name", Person::name)
            column("Birthday", Person::birthday)
            column("Age",Person::age)

            Observable.interval(1000, TimeUnit.MILLISECONDS)
                    .observeOnFx()
                    .subscribe {
                        column(it.toString(), Person::id)
                    }
        }

        listmenu(theme = "blue") {
            item(text = "Contacts") {
                // Marks this item as active.
                activeItem = this
                whenSelected { /* Do some action */ }
            }
            item(text = "Projects")
            item(text = "Settings")
        }
    }

}

class Person(val id: Int, val name: String, val birthday: LocalDate) {
    val age: Int get() = Period.between(birthday, LocalDate.now()).years
}
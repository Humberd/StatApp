package pl.swd.app.views.modals

import javafx.scene.Node
import tornadofx.Fragment

abstract class Modal(title: String? = null, icon: Node? = null) : Fragment(title, icon) {
    var status: ModalStatus = ModalStatus.PRISTINE

    override fun onUndock() {
        if (status === ModalStatus.PRISTINE) {
            status = ModalStatus.CANCELLED
        }
    }
}
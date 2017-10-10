package pl.swd.app.services;

import io.reactivex.subjects.BehaviorSubject
import org.springframework.stereotype.Service
import pl.swd.app.models.Project
import pl.swd.app.utils.asOptional
import pl.swd.app.utils.emptyOptional
import java.util.*

/**
 * This Service holds a currentProject state
 */
@Service
open class ProjectService {
    /**
     * Starting a behaviour subject with empty Project.
     * Need to wrap Project in Optional, because BehaviourSubject doesn't allow null
     */
    val currentProject: BehaviorSubject<Optional<Project>> = BehaviorSubject.createDefault(Project("Empty Project").asOptional())

    /**
     * Emits given project as a current Project
     */
    fun setCurrentProject(project: Project) {
        currentProject.onNext(project.asOptional())
    }

    fun unsetCurrentProject() {
        currentProject.onNext(emptyOptional())
    }

}
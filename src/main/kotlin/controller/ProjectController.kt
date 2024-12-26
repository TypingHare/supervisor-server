package supervisor.controller

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.ContextValue
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import supervisor.exception.ResourceNotFoundException
import supervisor.exception.ResourceUnauthorizedException
import supervisor.exception.ValidationException
import supervisor.model.Project
import supervisor.service.ProjectService
import supervisor.service.UserService

@Controller
class ProjectController(
    val projectService: ProjectService,
    val userService: UserService
) {
    @QueryMapping
    fun projectById(@Argument id: Long): Project =
        projectService.getProjectById(id)

    @QueryMapping
    fun projectsByUserId(
        @Argument userId: Long
    ): List<Project> =
        projectService.getProjectsByUserId(userId)

    @MutationMapping
    fun createProject(
        @Argument name: String,
        @ContextValue token: String
    ): Project {
        val user = userService.getUserByToken(token)
        return projectService.createProject(user.id, name)
    }

    @MutationMapping
    fun updateProjectName(
        @Argument id: Long,
        @Argument name: String,
        @ContextValue token: String
    ): Project {
        val project = checkProjectUpdateAuthorization(token, id)
        return projectService.updateProjectName(project, name)
    }

    @MutationMapping
    fun deleteProjectById(
        @Argument id: Long,
        @ContextValue token: String
    ): Project = projectService.deleteProject(
        checkProjectDeleteAuthorization(token, id)
    )

    @Throws(
        ValidationException::class,
        ResourceNotFoundException::class,
        ResourceUnauthorizedException::class
    )
    private fun checkProjectUpdateAuthorization(
        token: String,
        projectId: Long
    ): Project {
        val user = userService.getUserByToken(token)
        return projectService.getProjectById(projectId).apply {
            if (user.id != this.user?.id) {
                throw ResourceUnauthorizedException.PROJECT_UPDATE_NOT_ALLOWED
            }
        }
    }

    @Throws(
        ValidationException::class,
        ResourceNotFoundException::class,
        ResourceUnauthorizedException::class
    )
    private fun checkProjectDeleteAuthorization(
        token: String,
        projectId: Long
    ): Project {
        val user = userService.getUserByToken(token)
        return projectService.getProjectById(projectId).apply {
            if (user.id != this.user?.id) {
                throw ResourceUnauthorizedException.PROJECT_DELETE_NOT_ALLOWED
            }
        }
    }
}
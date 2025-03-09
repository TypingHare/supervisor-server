package supervisor.service

import jakarta.persistence.EntityManager
import org.springframework.stereotype.Service
import supervisor.exception.ResourceAlreadyExistException
import supervisor.exception.ResourceNotFoundException
import supervisor.exception.ResourceUnauthorizedException
import supervisor.model.Project
import supervisor.model.User
import supervisor.repository.ProjectRepository
import java.time.LocalDateTime

@Service
class ProjectService(
    private val projectRepository: ProjectRepository,
    private val entityManager: EntityManager,
    private val userService: UserService
) {
    @Throws(ResourceNotFoundException::class)
    fun getProjectById(id: Long): Project =
        projectRepository.findById(id)
            .orElseThrow { ResourceNotFoundException.PROJECT }

    fun getProjectsByUserId(userId: Long): List<Project> =
        projectRepository.findAllByUserId(userId)

    fun existProject(userId: Long, projectName: String): Boolean =
        projectRepository.findFirstByUserIdAndName(
            userId, projectName
        ).isPresent

    @Throws(ResourceAlreadyExistException::class)
    fun createProject(userId: Long, projectName: String): Project {
        if (existProject(userId, projectName)) {
            throw ResourceAlreadyExistException.PROJECT
        }

        return projectRepository.save(Project().apply {
            this.user = entityManager.getReference(User::class.java, userId)
            this.name = projectName
            this.createdAt = LocalDateTime.now()
            this.updatedAt = LocalDateTime.now()
        })
    }

    fun updateProjectName(project: Project, projectName: String): Project {
        val userId = project.user?.id!!
        if (existProject(userId, projectName)) {
            throw ResourceAlreadyExistException.PROJECT
        }

        return projectRepository.save(project.apply {
            this.name = projectName
        })
    }

    @Throws(ResourceUnauthorizedException::class)
    fun deleteProject(project: Project): Project =
        project.apply { projectRepository.delete(this) }
}
package supervisor.service

import jakarta.persistence.EntityManager
import org.springframework.stereotype.Service
import supervisor.exception.InvalidOperationException
import supervisor.exception.ResourceNotFoundException
import supervisor.model.Entry
import supervisor.model.User
import supervisor.repository.EntryRepository
import java.time.Duration
import java.time.LocalDateTime

@Service
class EntryService(
    private val entryRepository: EntryRepository,
    private val entityManager: EntityManager,
    private val projectService: ProjectService,
) {
    @Throws(ResourceNotFoundException::class)
    fun getEntryById(id: Long): Entry =
        entryRepository.findById(id).orElseThrow {
            ResourceNotFoundException.ENTRY
        }

    fun getEntriesByProjectId(projectId: Long): List<Entry> =
        entryRepository.findAllByProjectId(projectId)

    @Throws(InvalidOperationException::class)
    fun createEntry(userId: Long, projectId: Long): Entry {
        if (entryRepository.isProjectActive(projectId)) {
            throw InvalidOperationException.CREATE_ENTRY_WHEN_ONE_ALREADY_EXISTS
        }

        return entryRepository.save(Entry().apply {
            user = entityManager.getReference(User::class.java, userId)
            project = projectService.getProjectById(projectId)
        })
    }

    fun updateEntryDescription(id: Long, description: String): Entry =
        entryRepository.save(getEntryById(id).apply {
            this.description = description
        })

    @Throws(InvalidOperationException::class)
    fun startEntry(entry: Entry): Entry {
        if (entry.startedAt != null) {
            InvalidOperationException.START_STARTED_ENTRY
        }

        return entryRepository.save(entry.apply {
            startedAt = LocalDateTime.now()
        })
    }

    @Throws(InvalidOperationException::class)
    fun stopEntry(entry: Entry): Entry {
        if (entry.startedAt == null) {
            InvalidOperationException.STOP_NOT_STARTED_ENTRY
        }

        if (entry.stoppedAt != null) {
            InvalidOperationException.STOP_STOPPED_ENTRY
        }

        return entryRepository.save(entry.apply {
            stoppedAt = LocalDateTime.now()
            duration =
                Duration.between(startedAt, stoppedAt).toSeconds().toInt()
        })
    }
}
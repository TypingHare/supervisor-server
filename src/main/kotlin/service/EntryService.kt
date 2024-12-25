package supervisor.service

import org.springframework.stereotype.Service
import supervisor.common.throwIfNotNull
import supervisor.common.throwIfNull
import supervisor.exception.InvalidOperationException
import supervisor.exception.ResourceNotFoundException
import supervisor.model.Entry
import supervisor.repository.EntryRepository
import java.time.Duration
import java.time.LocalDateTime

@Service
class EntryService(
    private val entryRepository: EntryRepository,
    private val userService: UserService,
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
        throwIfNull(
            entryRepository.existsByProjectIdAndStoppedAtIsNull(projectId)
        ) {
            InvalidOperationException.CREATE_ENTRY_WHEN_ONE_ALREADY_EXISTS
        }

        return entryRepository.save(Entry().apply {
            user = userService.getUserReferenceById(userId)
            project = projectService.getProjectById(projectId)
        })
    }

    fun updateEntryDescription(id: Long, description: String): Entry =
        entryRepository.save(getEntryById(id).apply {
            this.description = description
        })

    @Throws(InvalidOperationException::class)
    fun startEntry(entry: Entry): Entry {
        throwIfNotNull(entry.startedAt) { InvalidOperationException.START_STARTED_ENTRY }
        return entryRepository.save(entry.apply {
            startedAt = LocalDateTime.now()
        })
    }

    @Throws(InvalidOperationException::class)
    fun stopEntry(entry: Entry): Entry {
        throwIfNull(entry.startedAt) { InvalidOperationException.STOP_NOT_STARTED_ENTRY }
        throwIfNotNull(entry.stoppedAt) { InvalidOperationException.STOP_STOPPED_ENTRY }

        return entryRepository.save(entry.apply {
            stoppedAt = LocalDateTime.now()
            duration =
                Duration.between(startedAt, stoppedAt).toSeconds().toInt()
        })
    }
}
package supervisor.controller

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.ContextValue
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import supervisor.exception.ResourceNotFoundException
import supervisor.exception.ResourceUnauthorizedException
import supervisor.exception.ValidationException
import supervisor.model.Entry
import supervisor.service.EntryService
import supervisor.service.UserService

@Controller
class EntryController(
    private val entryService: EntryService,
    private val userService: UserService,
) {
    @QueryMapping
    fun getEntriesByProjectId(@Argument projectId: Long): List<Entry> =
        entryService.getEntriesByProjectId(projectId)

    @MutationMapping
    fun createEntry(
        @Argument projectId: Long,
        @ContextValue token: String
    ): Entry {
        val user = userService.getUserByToken(token)
        return entryService.createEntry(user.id, projectId)
    }

    @Throws(
        ValidationException::class,
        ResourceNotFoundException::class,
        ResourceUnauthorizedException::class
    )
    @MutationMapping
    fun updateEntryDescription(
        @Argument id: Long,
        @Argument description: String,
        @ContextValue token: String
    ): Entry {
        checkEntryUpdateAuthorization(token, id)
        return entryService.updateEntryDescription(id, description)
    }

    @MutationMapping
    fun startEntry(
        @Argument id: Long,
        @ContextValue token: String
    ): Entry = entryService.startEntry(checkEntryUpdateAuthorization(token, id))

    @MutationMapping
    fun stopEntry(
        @Argument id: Long,
        @ContextValue token: String
    ): Entry = entryService.stopEntry(checkEntryUpdateAuthorization(token, id))

    /**
     * Retrieves the entry by the entry ID and checks if the user is allowed to
     * update it.
     *
     * @throws ValidationException if the user is not allowed to update the
     *                             entry.
     */
    @Throws(
        ValidationException::class,
        ResourceNotFoundException::class,
        ResourceUnauthorizedException::class
    )
    private fun checkEntryUpdateAuthorization(
        token: String,
        entryId: Long
    ): Entry {
        val user = userService.getUserByToken(token)
        return entryService.getEntryById(entryId).apply {
            if (user.id != this.user?.id) {
                throw ResourceUnauthorizedException.ENTRY_UPDATE_NOT_ALLOWED
            }
        }
    }
}
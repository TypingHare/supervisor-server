package supervisor.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import supervisor.model.Entry

@Repository
interface EntryRepository : JpaRepository<Entry, Long> {
    fun findAllByProjectId(projectId: Long): List<Entry>

    /**
     * Checks if a project has any active (running) entries.
     *
     * @param projectId The ID of the project to check
     * @return true if the project has at least one entry that hasn't been
     *         stopped yet, false otherwise
     */
    @Query(
        """
            SELECT CASE WHEN COUNT(e) > 0 THEN TRUE ELSE FALSE END 
            FROM Entry e 
            WHERE e.project.id = :projectId AND e.stoppedAt IS NULL
        """
    )
    fun isProjectActive(projectId: Long): Boolean
}
package supervisor.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import supervisor.model.Entry

@Repository
interface EntryRepository : JpaRepository<Entry, Long> {
    fun findAllByProjectId(projectId: Long): List<Entry>

    @Query(
        """
            SELECT CASE WHEN COUNT(e) > 0 THEN TRUE ELSE FALSE END 
            FROM Entry e 
            WHERE e.project.id = :projectId AND e.stoppedAt IS NULL
        """
    )
    fun existsByProjectIdAndStoppedAtIsNull(@Param("projectId") projectId: Long): Boolean
}
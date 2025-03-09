package supervisor.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import supervisor.model.Project
import java.util.*

@Repository
interface ProjectRepository : JpaRepository<Project, Long> {
    fun findAllByUserId(userId: Long): List<Project>

    fun findFirstByUserIdAndName(
        userId: Long,
        name: String
    ): Optional<Project>
}
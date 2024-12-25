package supervisor.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import supervisor.model.User
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>
    fun findByUsername(username: String): Optional<User>
}
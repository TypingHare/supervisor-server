package supervisor.model

import jakarta.persistence.*
import jakarta.validation.constraints.Size
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(name = "projects")
data class Project(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    var id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    var user: User? = null,

    @Column(name = "name", nullable = false, length = 32)
    @field:Size(
        max = 32,
        message = "Project name must not exceed 32 characters"
    )
    var name: String = "",

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime? = null,

    @Column(name = "stopped_at")
    var stoppedAt: LocalDateTime? = null
) : Serializable
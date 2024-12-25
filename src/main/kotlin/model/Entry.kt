package supervisor.model

import jakarta.persistence.*
import jakarta.validation.constraints.Size
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(name = "entries")
data class Entry(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    var id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    var user: User? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "project_id",
        nullable = false,
        referencedColumnName = "id"
    )
    var project: Project? = null,

    @Column(name = "description", length = 255, nullable = false)
    @field:Size(
        max = 255,
        message = "description must not exceed 255 characters"
    )
    var description: String = "",

    @Column(name = "duration", nullable = false)
    var duration: Int = 0,

    @Column(name = "started_at")
    var startedAt: LocalDateTime? = null,

    @Column(name = "stopped_at")
    var stoppedAt: LocalDateTime? = null
) : Serializable
package supervisor.model

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.io.Serializable

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    var id: Long = 0L,

    @Column(name = "email", nullable = false, length = 128, unique = true)
    @field:Email(message = "Email should be valid")
    @field:NotBlank(message = "Email is mandatory")
    @field:Size(max = 128, message = "Email must not exceed 128 characters")
    var email: String = "",

    @Column(name = "username", nullable = false, length = 32, unique = true)
    @field:NotBlank(message = "Username is mandatory")
    @field:Size(max = 32, message = "Username must not exceed 32 characters")
    var username: String = "",

    @Column(name = "auth_string", nullable = false, length = 128)
    @field:NotBlank(message = "Authentication string is mandatory")
    @field:Size(
        max = 128,
        message = "Authentication string must not exceed 128 characters"
    )
    var authString: String = ""
) : Serializable
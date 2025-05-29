@Entity
data class Usuario(
    @Id @GeneratedValue val id: Long = 0,
    val nome: String,
    val email: String,
    val nivelExperiencia: String, // Ex: "Iniciante", "Intermediário"

    @ManyToOne val academia: Academia
)


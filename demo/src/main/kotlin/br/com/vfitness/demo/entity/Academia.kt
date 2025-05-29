@Entity
data class Academia(
    @Id @GeneratedValue val id: Long = 0,
    val nome: String,
    val endereco: String
)

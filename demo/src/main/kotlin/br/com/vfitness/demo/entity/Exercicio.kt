@Entity
data class Exercicio(
    @Id @GeneratedValue val id: Long = 0,
    val nome: String,
    val grupoMuscular: String,
    val tipo: String, // Ex: "Máquina", "Livre"
    val equipamentosNecessarios: String // Ex: "Barra, Anilhas"
)


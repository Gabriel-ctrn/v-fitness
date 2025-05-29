@Entity
data class Maquina(
    @Id @GeneratedValue val id: Long = 0,
    val nome: String,
    val grupoMuscular: String,
    val status: String, // Ex: "Disponível", "Ocupada"

    @ManyToOne val academia: Academia
)

@Entity
data class Treino(
    @Id @GeneratedValue val id: Long = 0,

    @ManyToOne val usuario: Usuario,

    val data: LocalDate,
    val observacoes: String
)

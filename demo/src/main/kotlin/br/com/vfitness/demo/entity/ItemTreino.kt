@Entity
data class ItemTreino(
    @Id @GeneratedValue val id: Long = 0,

    @ManyToOne val treino: Treino,
    @ManyToOne val exercicio: Exercicio,

    val carga: Double,
    val repeticoes: Int,
    val series: Int
)

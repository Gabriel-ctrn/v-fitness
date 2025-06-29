import React, { useState, useEffect } from "react";
import {
  View,
  Text,
  TextInput,
  Button,
  StyleSheet,
  TouchableOpacity,
} from "react-native";
import { useLocalSearchParams, useRouter } from "expo-router";

interface Serie {
  peso: string;
  repeticoes: string;
}

interface Exercicio {
  id: number;
  nome: string;
  grupoMuscular: string;
  tipo?: string;
  carga?: number;
  series: Serie[];
  recomendacao?: string;
  treino?: { id: number };
}

export default function FluxoTreino() {
  const router = useRouter();
  const { treinoId, treinoNome } = useLocalSearchParams();
  const [exercicios, setExercicios] = useState<Exercicio[]>([]);
  const [exercicioIdx, setExercicioIdx] = useState(0);
  const [series, setSeries] = useState<Serie[]>([]);
  const [finalizado, setFinalizado] = useState(false);
  const [maquinaOcupada, setMaquinaOcupada] = useState(false);
  const [maquinaLoading, setMaquinaLoading] = useState(false);
  const [maquinaMsg, setMaquinaMsg] = useState("");
  const [loading, setLoading] = useState(true);
  const [treinoFinalizado, setTreinoFinalizado] = useState(false);

  useEffect(() => {
    if (treinoId) {
      fetch(`http://localhost:8080/exercicios/treino/${treinoId}`)
        .then((resp) => resp.json())
        .then((data) => {
          // Transforma cada exercício para garantir que series seja um array de objetos com os dados cadastrados
          const exerciciosTratados = (data || []).map((ex: any) => {
            let seriesArr: Serie[] = [];
            if (Array.isArray(ex.cargas) && Array.isArray(ex.repeticoes)) {
              seriesArr = ex.cargas.map((peso: number, idx: number) => ({
                peso: peso?.toString() ?? "",
                repeticoes: ex.repeticoes[idx] ?? "",
              }));
            } else if (typeof ex.series === "number") {
              seriesArr = Array.from({ length: ex.series }, () => ({
                peso: "",
                repeticoes: "",
              }));
            }
            return { ...ex, series: seriesArr };
          });
          setExercicios(exerciciosTratados);
          if (exerciciosTratados.length > 0)
            setSeries(exerciciosTratados[0].series || []);
        })
        .finally(() => setLoading(false));
    }
  }, [treinoId]);

  useEffect(() => {
    if (exercicios.length > 0) {
      setSeries(exercicios[exercicioIdx].series || []);
    }
  }, [exercicioIdx, exercicios]);

  const handleEditSerie = (
    idx: number,
    field: "peso" | "repeticoes",
    value: string
  ) => {
    setSeries((prev) =>
      prev.map((s, i) => (i === idx ? { ...s, [field]: value } : s))
    );
  };

  // Função para buscar exercícios do backend novamente
  const buscarExercicios = async () => {
    setLoading(true);
    try {
      const resp = await fetch(
        `http://localhost:8080/exercicios/treino/${treinoId}`
      );
      const data = await resp.json();
      const exerciciosTratados = (data || []).map((ex: any) => {
        let seriesArr: Serie[] = [];
        if (Array.isArray(ex.cargas) && Array.isArray(ex.repeticoes)) {
          seriesArr = ex.cargas.map((peso: number, idx: number) => ({
            peso: peso?.toString() ?? "",
            repeticoes: ex.repeticoes[idx] ?? "",
          }));
        } else if (typeof ex.series === "number") {
          seriesArr = Array.from({ length: ex.series }, () => ({
            peso: "",
            repeticoes: "",
          }));
        }
        return { ...ex, series: seriesArr };
      });
      setExercicios(exerciciosTratados);
      if (exerciciosTratados.length > 0)
        setSeries(exerciciosTratados[exercicioIdx]?.series || []);
    } finally {
      setLoading(false);
    }
  };

  // Salvar alterações no backend
  const handleSalvar = async () => {
    const cargas = series.map((s) => Number(s.peso));
    const repeticoes = series.map((s) => s.repeticoes);
    try {
      await fetch(`http://localhost:8080/exercicios/${exercicio.id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          ...exercicio,
          cargas,
          repeticoes,
          series: series.length,
          treino: { id: exercicio.treino?.id || treinoId },
        }),
      });
      await buscarExercicios(); // Atualiza os dados após salvar
    } catch (e) {
      // Pode exibir mensagem de erro se quiser
    }
  };

  const handleFinalizarExercicio = () => {
    if (exercicioIdx < exercicios.length - 1) {
      setExercicioIdx((i) => i + 1);
      setFinalizado(false);
    } else {
      setTreinoFinalizado(true);
      setTimeout(() => {
        router.replace("/home-usuario");
      }, 2000);
    }
  };

  const handleMaquinaOcupada = () => {
    setMaquinaLoading(true);
    setMaquinaMsg("");
    setTimeout(() => {
      setMaquinaLoading(false);
      setMaquinaMsg("vá fazer outra coisa");
    }, 2000);
  };

  if (loading) return <Text>Carregando...</Text>;
  if (exercicios.length === 0) return <Text>Nenhum exercício encontrado.</Text>;
  if (treinoFinalizado) {
    return (
      <View style={styles.container}>
        <Text style={styles.tituloTreino}>Treino finalizado!</Text>
      </View>
    );
  }

  const exercicio = exercicios[exercicioIdx];

  return (
    <View style={styles.container}>
      <Text style={styles.tituloTreino}>
        Treino de {treinoNome || exercicio.grupoMuscular}
      </Text>
      <Text style={styles.nomeExercicio}>{exercicio.nome}</Text>
      <View style={styles.seriesBox}>
        {series.map((serie, idx) => (
          <View key={idx} style={styles.serieLinha}>
            <Text style={styles.serieNum}>{idx + 1}</Text>
            <TextInput
              style={styles.input}
              value={serie.peso}
              onChangeText={(v) => handleEditSerie(idx, "peso", v)}
              keyboardType="numeric"
              placeholder="Peso (kg)"
            />
            <TextInput
              style={styles.input}
              value={serie.repeticoes}
              onChangeText={(v) => handleEditSerie(idx, "repeticoes", v)}
              keyboardType="numeric"
              placeholder="Repetições"
            />
          </View>
        ))}
      </View>
      <Button title="Salvar" onPress={handleSalvar} />
      <Text style={styles.recomendacao}>{exercicio.recomendacao || ""}</Text>
      <View style={styles.botoesBox}>
        <TouchableOpacity
          style={[styles.botao, finalizado && styles.botaoAtivo]}
          onPress={() => {
            setFinalizado((v) => !v);
            if (!finalizado) handleFinalizarExercicio();
          }}
        >
          <Text style={styles.botaoText}>
            {finalizado ? "Finalizado" : "Finalizar"}
          </Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={[styles.botao, maquinaOcupada && styles.botaoAtivo]}
          onPress={() => {
            setMaquinaOcupada((v) => !v);
            if (!maquinaOcupada) handleMaquinaOcupada();
          }}
        >
          <Text style={styles.botaoText}>
            {maquinaOcupada ? "Máquina Ocupada" : "Máquina Livre"}
          </Text>
        </TouchableOpacity>
      </View>
      {maquinaLoading && (
        <Text style={{ color: "#007700", fontWeight: "bold" }}>
          Carregando...
        </Text>
      )}
      {maquinaMsg && (
        <Text style={{ color: "#007700", fontWeight: "bold" }}>
          {maquinaMsg}
        </Text>
      )}
      <View style={{ flexDirection: "row", justifyContent: "space-between" }}>
        <Button
          title="Anterior"
          onPress={() => setExercicioIdx((i) => Math.max(0, i - 1))}
          disabled={exercicioIdx === 0}
        />
        <Button
          title="Próximo"
          onPress={() =>
            setExercicioIdx((i) => Math.min(exercicios.length - 1, i + 1))
          }
          disabled={exercicioIdx === exercicios.length - 1}
        />
      </View>
      <Button title="Voltar" onPress={() => router.back()} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, padding: 20, backgroundColor: "#fff" },
  tituloTreino: { fontSize: 22, fontWeight: "bold", marginBottom: 10 },
  nomeExercicio: { fontSize: 20, fontWeight: "bold", marginBottom: 16 },
  seriesBox: { marginBottom: 16 },
  serieLinha: { flexDirection: "row", alignItems: "center", marginBottom: 8 },
  serieNum: { width: 24, fontWeight: "bold", fontSize: 16 },
  input: {
    borderWidth: 1,
    borderColor: "#ccc",
    borderRadius: 5,
    padding: 8,
    marginHorizontal: 4,
    width: 70,
    textAlign: "center",
  },
  recomendacao: { marginBottom: 16, color: "#007700", fontWeight: "bold" },
  botoesBox: { flexDirection: "row", gap: 12, marginBottom: 20 },
  botao: {
    backgroundColor: "#A1CEDC",
    borderRadius: 5,
    paddingVertical: 12,
    paddingHorizontal: 18,
    marginRight: 8,
  },
  botaoAtivo: { backgroundColor: "#007700" },
  botaoText: { color: "#fff", fontWeight: "bold" },
});

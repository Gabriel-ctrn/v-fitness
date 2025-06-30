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
import { Colors } from "../constants/Colors";

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

  // Salvar alterações no backend automaticamente ao editar série
  useEffect(() => {
    if (!loading && exercicios.length > 0) {
      const exercicio = exercicios[exercicioIdx];
      const cargas = series.map((s) => Number(s.peso));
      const repeticoes = series.map((s) => s.repeticoes);
      fetch(`http://localhost:8080/exercicios/${exercicio.id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          ...exercicio,
          cargas,
          repeticoes,
          series: series.length,
          treino: { id: Number(treinoId) },
        }),
      });
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [series]);

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

  const handleMaquinaOcupada = async () => {
    setMaquinaLoading(true);
    setMaquinaMsg("");
    try {
      const resp = await fetch(
        "http://localhost:8080/assistentes/sugestao-exercicio",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            treinoId: Number(treinoId),
            exercicioId: exercicio.id,
          }),
        }
      );
      if (resp.ok) {
        let sugestao = await resp.text();
        // Tenta extrair apenas o texto formatado da IA caso venha um JSON Gemini
        try {
          const json = JSON.parse(sugestao);
          if (json?.candidates?.[0]?.content?.parts?.[0]?.text) {
            sugestao = json.candidates[0].content.parts[0].text;
          }
        } catch {}
        setTimeout(() => {
          setMaquinaLoading(false);
          setMaquinaMsg(sugestao);
        }, 2000);
      } else {
        setTimeout(() => {
          setMaquinaLoading(false);
          setMaquinaMsg("Erro ao obter sugestão da IA");
        }, 2000);
      }
    } catch {
      setTimeout(() => {
        setMaquinaLoading(false);
        setMaquinaMsg("Erro ao obter sugestão da IA");
      }, 2000);
    }
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
      <View style={styles.seriesBoxCustom}>
        {series.map((serie, idx) => (
          <View key={idx} style={styles.serieLinhaCustom}>
            <Text style={styles.serieNumCustom}>{idx + 1}ª</Text>
            <TextInput
              style={styles.inputCustom}
              value={serie.peso}
              onChangeText={(v) => handleEditSerie(idx, "peso", v)}
              keyboardType="numeric"
              placeholder="Carga (kg)"
              placeholderTextColor="#aaa"
            />
            <Text style={styles.xText}>x</Text>
            <TextInput
              style={styles.inputCustom}
              value={serie.repeticoes}
              onChangeText={(v) => handleEditSerie(idx, "repeticoes", v)}
              keyboardType="numeric"
              placeholder="Reps"
              placeholderTextColor="#aaa"
            />
            <Text style={styles.repsLabel}>reps</Text>
          </View>
        ))}
      </View>
      <Text style={styles.recomendacao}>{exercicio.recomendacao || ""}</Text>
      <View style={styles.botoesBox}>
        <TouchableOpacity
          style={[styles.botaoFinalizar, finalizado && styles.botaoAtivo]}
          onPress={() => {
            setFinalizado((v) => !v);
            if (!finalizado) handleFinalizarExercicio();
          }}
        >
          <Text style={styles.botaoFinalizarText}>
            {finalizado ? "Finalizado" : "Finalizar"}
          </Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={[
            styles.botaoSecundario,
            maquinaOcupada && styles.botaoSecundarioAtivo,
            // Remover styles.botaoAtivo para não sobrepor o vermelho
          ]}
          onPress={() => {
            setMaquinaOcupada((v) => !v);
            if (!maquinaOcupada) handleMaquinaOcupada();
          }}
        >
          <Text style={[styles.botaoSecundarioText, maquinaOcupada && { color: '#fff' }]}>Máquina ocupada</Text>
        </TouchableOpacity>
      </View>
      {maquinaLoading && (
        <Text style={{ color: "#007700", fontWeight: "bold" }}>
          Carregando...
        </Text>
      )}
      {maquinaMsg && (
        <Text
          style={{
            color: "#007700",
            fontWeight: "bold",
            marginTop: 10,
          }}
        >
          {maquinaMsg}
        </Text>
      )}
      <TouchableOpacity
        style={styles.voltarBtn}
        onPress={() => router.back()}
      >
        <Text style={styles.voltarBtnText}>Voltar</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, padding: 20, backgroundColor: Colors.light.background },
  tituloTreino: { fontSize: 22, fontWeight: "bold", marginBottom: 10 },
  nomeExercicio: { fontSize: 20, fontWeight: "bold", marginBottom: 16 },
  seriesBoxCustom: {
    marginBottom: 16,
    alignItems: "center",
    justifyContent: "center",
    width: "100%",
  },
  serieLinhaCustom: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    backgroundColor: "#f7f7fa",
    borderRadius: 10,
    paddingVertical: 10,
    paddingHorizontal: 16,
    marginBottom: 10,
    shadowColor: "#000",
    shadowOpacity: 0.04,
    shadowRadius: 4,
    elevation: 1,
    width: "90%",
    alignSelf: "center",
  },
  serieNumCustom: {
    fontWeight: "bold",
    fontSize: 18,
    color: Colors.light.tint,
    marginRight: 10,
    width: 32,
    textAlign: "center",
  },
  inputCustom: {
    borderWidth: 1,
    borderColor: Colors.light.tint,
    borderRadius: 8,
    padding: 10,
    marginHorizontal: 6,
    width: 80,
    backgroundColor: "#fff",
    fontSize: 16,
    textAlign: "center",
    color: Colors.light.text,
  },
  xText: {
    fontWeight: "bold",
    fontSize: 18,
    color: Colors.light.tint,
    marginHorizontal: 4,
  },
  repsLabel: {
    fontSize: 14,
    color: Colors.light.text,
    marginLeft: 4,
    fontWeight: "bold",
  },
  recomendacao: {
    marginBottom: 16,
    color: Colors.light.tint,
    fontWeight: "bold",
  },
  botoesBox: { flexDirection: "row", gap: 12, marginBottom: 20 },
  botaoFinalizar: {
    backgroundColor: Colors.light.tint,
    borderRadius: 8,
    paddingVertical: 14,
    paddingHorizontal: 24,
    alignItems: "center",
    minWidth: 140,
    marginRight: 8,
  },
  botaoFinalizarText: {
    color: Colors.light.background,
    fontWeight: "bold",
    fontSize: 16,
    textAlign: "center",
  },
  botaoSecundario: {
    backgroundColor: Colors.light.background,
    borderRadius: 8,
    paddingVertical: 14,
    paddingHorizontal: 24,
    alignItems: "center",
    borderWidth: 1,
    borderColor: Colors.light.tint,
    minWidth: 140,
  },
  botaoSecundarioText: {
    color: Colors.light.tint,
    fontWeight: "bold",
    fontSize: 16,
    textAlign: "center",
  },
  botaoSecundarioAtivo: {
    backgroundColor: '#ff4d4d', // vermelho claro mais visível
    borderColor: '#e00',
  },
  botaoAtivo: {
    backgroundColor: Colors.light.text,
    opacity: 0.7,
  },
  voltarBtn: {
    marginTop: 24,
    backgroundColor: Colors.light.tint,
    borderRadius: 8,
    paddingVertical: 14,
    alignItems: "center",
  },
  voltarBtnText: {
    color: Colors.light.background,
    fontWeight: "bold",
    fontSize: 16,
    textAlign: "center",
  },
});

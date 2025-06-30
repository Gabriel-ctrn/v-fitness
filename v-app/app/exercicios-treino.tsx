import React, { useEffect, useState } from "react";
import {
  View,
  Text,
  Button,
  FlatList,
  TextInput,
  StyleSheet,
  TouchableOpacity,
} from "react-native";
import { useLocalSearchParams, useRouter } from "expo-router";
import { Colors } from "../constants/Colors";

interface Exercicio {
  id: number;
  nome: string;
  grupoMuscular: string;
  tipo: string;
  carga?: number;
  series?: number;
  repeticoes?: string;
}

export default function ExerciciosTreino() {
  const { treinoId } = useLocalSearchParams();
  const navigation = useRouter();
  const [exercicios, setExercicios] = useState<Exercicio[]>([]);
  const [novoNome, setNovoNome] = useState("");
  const [novoGrupo, setNovoGrupo] = useState("");
  const [novoTipo, setNovoTipo] = useState("");
  const [novoCarga, setNovoCarga] = useState("");
  const [novoSeries, setNovoSeries] = useState("");
  const [novoReps, setNovoReps] = useState("");
  const [cargas, setCargas] = useState<string[]>([]);
  const [reps, setReps] = useState<string[]>([]);
  const [loading, setLoading] = useState(false);
  const [mensagem, setMensagem] = useState("");
  const [editandoId, setEditandoId] = useState<number | null>(null);

  useEffect(() => {
    if (treinoId) {
      buscarExercicios();
    }
  }, [treinoId]);

  const buscarExercicios = async () => {
    setLoading(true);
    try {
      const resp = await fetch(
        `http://localhost:8080/exercicios/treino/${treinoId}`
      );
      if (!resp.ok) throw new Error("Erro ao buscar exercícios");
      const data = await resp.json();
      setExercicios(data);
    } catch (e) {
      setMensagem("Erro ao buscar exercícios");
    } finally {
      setLoading(false);
    }
  };

  const handleSeriesChange = (value: string) => {
    setNovoSeries(value);
    const n = parseInt(value) || 0;
    setCargas(Array(n).fill(""));
    setReps(Array(n).fill(""));
  };

  const handleCargaChange = (idx: number, value: string) => {
    setCargas((prev) => prev.map((c, i) => (i === idx ? value : c)));
  };
  const handleRepChange = (idx: number, value: string) => {
    setReps((prev) => prev.map((r, i) => (i === idx ? value : r)));
  };

  const handleAdicionarExercicio = async () => {
    if (
      !novoNome.trim() ||
      !novoGrupo.trim() ||
      !novoTipo.trim() ||
      cargas.some((c) => !c.trim()) ||
      reps.some((r) => !r.trim()) ||
      !novoSeries.trim()
    )
      return;
    setLoading(true);
    try {
      const resp = await fetch("http://localhost:8080/exercicios", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          nome: novoNome,
          grupoMuscular: novoGrupo,
          tipo: novoTipo,
          cargas: cargas.map(Number),
          series: parseInt(novoSeries),
          repeticoes: reps,
          treinoId: Number(treinoId),
        }),
      });
      if (!resp.ok) throw new Error("Erro ao adicionar exercício");
      setMensagem("Exercício adicionado!");
      setNovoNome("");
      setNovoGrupo("");
      setNovoTipo("");
      setNovoCarga("");
      setNovoSeries("");
      setNovoReps("");
      setCargas([]);
      setReps([]);
      buscarExercicios();
    } catch (e) {
      setMensagem("Erro ao adicionar exercício");
    } finally {
      setLoading(false);
    }
  };

  const handleEditarExercicio = (exercicio: Exercicio) => {
    setEditandoId(exercicio.id);
    setNovoNome(exercicio.nome);
    setNovoGrupo(exercicio.grupoMuscular);
    setNovoTipo(exercicio.tipo);
    setNovoSeries(exercicio.series ? exercicio.series.toString() : "");
    setCargas(exercicio.carga ? [exercicio.carga.toString()] : []);
    setReps(Array.isArray(exercicio.repeticoes) ? exercicio.repeticoes : exercicio.repeticoes ? [exercicio.repeticoes] : []);
  };

  const handleSalvarExercicio = async () => {
    if (
      !novoNome.trim() ||
      !novoGrupo.trim() ||
      !novoTipo.trim() ||
      cargas.some((c) => !c.trim()) ||
      reps.some((r) => !r.trim()) ||
      !novoSeries.trim()
    )
      return;
    setLoading(true);
    try {
      const url = editandoId
        ? `http://localhost:8080/exercicios/${editandoId}`
        : "http://localhost:8080/exercicios";
      const method = editandoId ? "PUT" : "POST";
      const body = editandoId
        ? JSON.stringify({
            id: editandoId,
            nome: novoNome,
            grupoMuscular: novoGrupo,
            tipo: novoTipo,
            cargas: cargas.map(Number),
            series: parseInt(novoSeries),
            repeticoes: reps,
            treino: { id: Number(treinoId) },
          })
        : JSON.stringify({
            nome: novoNome,
            grupoMuscular: novoGrupo,
            tipo: novoTipo,
            cargas: cargas.map(Number),
            series: parseInt(novoSeries),
            repeticoes: reps,
            treinoId: Number(treinoId),
          });
      const resp = await fetch(url, {
        method,
        headers: { "Content-Type": "application/json" },
        body,
      });
      if (!resp.ok) throw new Error("Erro ao salvar exercício");
      setMensagem(editandoId ? "Exercício atualizado!" : "Exercício adicionado!");
      setNovoNome("");
      setNovoGrupo("");
      setNovoTipo("");
      setNovoCarga("");
      setNovoSeries("");
      setNovoReps("");
      setCargas([]);
      setReps([]);
      setEditandoId(null);
      buscarExercicios();
    } catch (e) {
      setMensagem("Erro ao salvar exercício");
    } finally {
      setLoading(false);
    }
  };

  const handleExcluirExercicio = async (id: number) => {
    if (!window.confirm("Deseja realmente excluir este exercício?")) return;
    setLoading(true);
    try {
      const resp = await fetch(`http://localhost:8080/exercicios/${id}`, {
        method: "DELETE",
      });
      if (!resp.ok) throw new Error("Erro ao excluir exercício");
      setMensagem("Exercício excluído!");
      buscarExercicios();
    } catch (e) {
      setMensagem("Erro ao excluir exercício");
    } finally {
      setLoading(false);
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Exercícios do Treino</Text>
      {loading && <Text>Carregando...</Text>}
      <FlatList
        data={exercicios}
        keyExtractor={(item) => item.id.toString()}
        horizontal
        showsHorizontalScrollIndicator={false}
        contentContainerStyle={{ gap: 16, marginBottom: 24 }}
        renderItem={({ item }) => (
          <View style={styles.card}>
            <Text style={styles.cardTitle} numberOfLines={2} ellipsizeMode="tail">{item.nome}</Text>
            <Text style={styles.cardText}>Grupo Muscular: {item.grupoMuscular}</Text>
            <Text style={styles.cardText}>Tipo: {item.tipo}</Text>
            <Text style={styles.cardText}>Séries: {item.series}</Text>
            <View style={{ flexDirection: 'row', marginTop: 8, gap: 8 }}>
              <TouchableOpacity style={styles.editBtn} onPress={() => handleEditarExercicio(item)}>
                <Text style={styles.editBtnText}>Editar</Text>
              </TouchableOpacity>
              <TouchableOpacity style={styles.deleteBtn} onPress={() => handleExcluirExercicio(item.id)}>
                <Text style={styles.deleteBtnText}>Excluir</Text>
              </TouchableOpacity>
            </View>
          </View>
        )}
      />
      <View style={styles.addBox}>
        <TextInput
          style={styles.input}
          placeholder="Nome do exercício"
          value={novoNome}
          onChangeText={setNovoNome}
        />
        <TextInput
          style={styles.input}
          placeholder="Grupo Muscular"
          value={novoGrupo}
          onChangeText={setNovoGrupo}
        />
        <TextInput
          style={styles.input}
          placeholder="Tipo (ex: força, cardio)"
          value={novoTipo}
          onChangeText={setNovoTipo}
        />
        <TextInput
          style={styles.input}
          placeholder="Séries"
          value={novoSeries}
          onChangeText={handleSeriesChange}
          keyboardType="numeric"
        />
        {cargas.map((c, idx) => (
          <View
            key={idx}
            style={{
              flexDirection: "row",
              alignItems: "center",
              marginBottom: 8,
            }}
          >
            <Text style={{ marginRight: 4 }}>Série {idx + 1}:</Text>
            <TextInput
              style={[styles.input, { width: 60 }]}
              placeholder="Carga (kg)"
              value={c}
              onChangeText={(v) => handleCargaChange(idx, v)}
              keyboardType="numeric"
            />
            <TextInput
              style={[styles.input, { width: 60, marginLeft: 4 }]}
              placeholder="Reps"
              value={reps[idx]}
              onChangeText={(v) => handleRepChange(idx, v)}
              keyboardType="numeric"
            />
          </View>
        ))}
        <TouchableOpacity
          style={[styles.addBtn, loading && { opacity: 0.6 }]}
          onPress={handleSalvarExercicio}
          disabled={loading}
        >
          <Text style={styles.addBtnText}>{editandoId ? "Salvar" : "Adicionar"}</Text>
        </TouchableOpacity>
      </View>
      {mensagem ? <Text style={styles.mensagem}>{mensagem}</Text> : null}
      <TouchableOpacity style={styles.voltarBtn} onPress={() => navigation.back()}>
        <Text style={styles.voltarBtnText}>Voltar</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, padding: 20, backgroundColor: Colors.light.background },
  title: { fontSize: 24, fontWeight: "bold", marginBottom: 20 },
  exercicioItem: {
    padding: 18,
    borderBottomWidth: 1,
    borderBottomColor: Colors.light.text,
    backgroundColor: Colors.light.background,
    borderRadius: 8,
    marginBottom: 10,
  },
  addBox: {
    flexDirection: "column",
    alignItems: "stretch",
    marginTop: 28,
    gap: 16,
    padding: 16,
    backgroundColor: Colors.light.background,
    borderRadius: 10,
    shadowColor: Colors.light.text,
    shadowOpacity: 0.08,
    shadowRadius: 8,
    elevation: 2,
  },
  addBtn: {
    backgroundColor: Colors.light.tint,
    borderRadius: 8,
    paddingVertical: 14,
    alignItems: "center",
    marginTop: 8,
  },
  addBtnText: {
    color: Colors.light.background,
    fontWeight: "bold",
    fontSize: 16,
  },
  voltarBtn: {
    marginTop: 32,
    backgroundColor: Colors.light.tint,
    borderRadius: 8,
    paddingVertical: 14,
    alignItems: "center",
  },
  voltarBtnText: {
    color: Colors.light.background,
    fontWeight: "bold",
    fontSize: 16,
  },
  input: {
    flex: 1,
    borderWidth: 1,
    borderColor: Colors.light.text,
    borderRadius: 8,
    padding: 14,
    marginRight: 8,
    color: Colors.light.text,
    backgroundColor: Colors.light.background,
    marginBottom: 6,
  },
  mensagem: {
    marginTop: 20,
    color: Colors.light.tint,
    fontWeight: "bold",
    textAlign: "center",
  },
  card: {
    backgroundColor: Colors.light.tint,
    borderRadius: 14,
    padding: 18,
    width: 180,
    marginRight: 0,
    shadowColor: Colors.light.text,
    shadowOpacity: 0.08,
    shadowRadius: 8,
    elevation: 2,
    justifyContent: "center",
    alignItems: "flex-start",
  },
  cardTitle: {
    color: Colors.light.background,
    fontWeight: "bold",
    fontSize: 18,
    marginBottom: 6,
  },
  cardText: {
    color: Colors.light.background,
    fontSize: 14,
    marginBottom: 2,
  },
  editBtn: {
    backgroundColor: Colors.light.background,
    borderRadius: 6,
    paddingVertical: 6,
    paddingHorizontal: 12,
    borderWidth: 1,
    borderColor: Colors.light.tint,
  },
  editBtnText: {
    color: Colors.light.tint,
    fontWeight: "bold",
  },
  deleteBtn: {
    backgroundColor: '#fff0f0',
    borderRadius: 6,
    paddingVertical: 6,
    paddingHorizontal: 12,
    borderWidth: 1,
    borderColor: '#e00',
  },
  deleteBtnText: {
    color: '#e00',
    fontWeight: "bold",
  },
});

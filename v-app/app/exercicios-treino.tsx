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
  const [loading, setLoading] = useState(false);
  const [mensagem, setMensagem] = useState("");

  useEffect(() => {
    buscarExercicios();
  }, []);

  const buscarExercicios = async () => {
    setLoading(true);
    try {
      const resp = await fetch("http://localhost:8080/exercicios");
      if (!resp.ok) throw new Error("Erro ao buscar exercícios");
      const data = await resp.json();
      setExercicios(data);
    } catch (e) {
      setMensagem("Erro ao buscar exercícios");
    } finally {
      setLoading(false);
    }
  };

  const handleAdicionarExercicio = async () => {
    if (
      !novoNome.trim() ||
      !novoGrupo.trim() ||
      !novoTipo.trim() ||
      !novoCarga.trim() ||
      !novoSeries.trim() ||
      !novoReps.trim()
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
          carga: parseFloat(novoCarga),
          series: parseInt(novoSeries),
          repeticoes: novoReps,
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
      buscarExercicios();
    } catch (e) {
      setMensagem("Erro ao adicionar exercício");
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
        renderItem={({ item }) => (
          <View style={styles.exercicioItem}>
            <Text style={{ fontWeight: "bold" }}>{item.nome}</Text>
            <Text>Grupo Muscular: {item.grupoMuscular}</Text>
            <Text>Tipo: {item.tipo}</Text>

            <Text>Carga: {item.carga} kg</Text>

            <Text>Séries: {item.series}</Text>

            {item.repeticoes && <Text>Repetições: {item.repeticoes}</Text>}
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
          placeholder="Carga (kg)"
          value={novoCarga}
          onChangeText={setNovoCarga}
          keyboardType="numeric"
        />
        <TextInput
          style={styles.input}
          placeholder="Nº de séries"
          value={novoSeries}
          onChangeText={setNovoSeries}
          keyboardType="numeric"
        />
        <TextInput
          style={styles.input}
          placeholder="Repetições por série (ex: 12,12,10)"
          value={novoReps}
          onChangeText={setNovoReps}
        />
        <Button
          title="Adicionar"
          onPress={handleAdicionarExercicio}
          disabled={loading}
        />
      </View>
      {mensagem ? <Text style={styles.mensagem}>{mensagem}</Text> : null}
      <Button title="Voltar" onPress={() => navigation.back()} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, padding: 20, backgroundColor: "#fff" },
  title: { fontSize: 24, fontWeight: "bold", marginBottom: 20 },
  exercicioItem: {
    padding: 12,
    borderBottomWidth: 1,
    borderBottomColor: "#eee",
  },
  addBox: { flexDirection: "row", alignItems: "center", marginTop: 16, gap: 8 },
  input: {
    flex: 1,
    borderWidth: 1,
    borderColor: "#ccc",
    borderRadius: 5,
    padding: 10,
    marginRight: 8,
  },
  mensagem: {
    marginTop: 20,
    color: "#007700",
    fontWeight: "bold",
    textAlign: "center",
  },
});

import React, { useState } from "react";
import {
  View,
  Text,
  Button,
  FlatList,
  StyleSheet,
  TouchableOpacity,
  TextInput,
} from "react-native";

const MOCK_PLANOS = [
  {
    id: 1,
    nome: "Plano Hipertrofia",
    objetivo: "Ganho de massa muscular",
    treinos: [
      { id: 1, nome: "Treino Superiores" },
      { id: 2, nome: "Treino Inferiores" },
      { id: 3, nome: "Treino HIIT" },
    ],
  },
];

export default function HomeUsuario() {
  const [planoSelecionado, setPlanoSelecionado] = useState(MOCK_PLANOS[0]);
  const [mensagem, setMensagem] = useState("");
  const [novoTreino, setNovoTreino] = useState("");

  const handleIniciarTreino = (treinoNome: string) => {
    setMensagem(`Treino "${treinoNome}" iniciado! (mock)`);
    setTimeout(() => setMensagem(""), 2000);
  };

  const handleAdicionarTreino = () => {
    if (!novoTreino.trim()) return;
    const novo = { id: planoSelecionado.treinos.length + 1, nome: novoTreino };
    setPlanoSelecionado({
      ...planoSelecionado,
      treinos: [...planoSelecionado.treinos, novo],
    });
    setNovoTreino("");
    setMensagem("Treino adicionado!");
    setTimeout(() => setMensagem(""), 1500);
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Bem-vindo ao V-Fitness!</Text>
      <Text style={styles.subtitle}>Plano de Treino Atual:</Text>
      <View style={styles.planoBox}>
        <Text style={styles.planoNome}>{planoSelecionado.nome}</Text>
        <Text>Objetivo: {planoSelecionado.objetivo}</Text>
        <Text style={styles.treinosTitle}>Treinos disponíveis:</Text>
        <FlatList
          data={planoSelecionado.treinos}
          keyExtractor={(item) => item.id.toString()}
          renderItem={({ item }) => (
            <TouchableOpacity
              style={styles.treinoBtn}
              onPress={() => handleIniciarTreino(item.nome)}
            >
              <Text style={styles.treinoBtnText}>{item.nome}</Text>
            </TouchableOpacity>
          )}
        />
        <View style={styles.addTreinoBox}>
          <TextInput
            style={styles.input}
            placeholder="Novo treino (ex: Abdômen)"
            value={novoTreino}
            onChangeText={setNovoTreino}
          />
          <Button title="Adicionar Treino" onPress={handleAdicionarTreino} />
        </View>
      </View>
      {mensagem ? <Text style={styles.mensagem}>{mensagem}</Text> : null}
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, padding: 20, backgroundColor: "#fff" },
  title: {
    fontSize: 28,
    fontWeight: "bold",
    marginBottom: 20,
    textAlign: "center",
  },
  subtitle: { fontSize: 18, fontWeight: "bold", marginBottom: 10 },
  planoBox: {
    backgroundColor: "#f2f2f2",
    borderRadius: 8,
    padding: 16,
    marginBottom: 20,
  },
  planoNome: { fontSize: 20, fontWeight: "bold", marginBottom: 4 },
  treinosTitle: { marginTop: 10, fontWeight: "bold" },
  treinoBtn: {
    backgroundColor: "#A1CEDC",
    borderRadius: 5,
    padding: 12,
    marginTop: 10,
  },
  treinoBtnText: { color: "#1D3D47", fontWeight: "bold", textAlign: "center" },
  mensagem: {
    marginTop: 20,
    color: "#007700",
    fontWeight: "bold",
    textAlign: "center",
  },
  addTreinoBox: {
    flexDirection: "row",
    alignItems: "center",
    marginTop: 16,
    gap: 8,
  },
  input: {
    flex: 1,
    borderWidth: 1,
    borderColor: "#ccc",
    borderRadius: 5,
    padding: 10,
    marginRight: 8,
  },
});

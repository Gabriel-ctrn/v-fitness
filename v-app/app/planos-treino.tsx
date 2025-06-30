import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  Button,
  FlatList,
  StyleSheet,
} from "react-native";
import { Colors } from "../constants/Colors";

interface PlanoTreino {
  id: number;
  nome: string;
  objetivo: string;
  usuario: { id: number; nome: string };
}

const MOCK_PLANOS: PlanoTreino[] = [
  {
    id: 1,
    nome: "Hipertrofia",
    objetivo: "Ganho de massa muscular",
    usuario: { id: 1, nome: "Maria Souza" },
  },
  {
    id: 2,
    nome: "Emagrecimento",
    objetivo: "Perda de gordura",
    usuario: { id: 2, nome: "João Silva" },
  },
];

export default function PlanosTreino() {
  const [planos, setPlanos] = useState<PlanoTreino[]>(MOCK_PLANOS);
  const [nome, setNome] = useState("");
  const [objetivo, setObjetivo] = useState("");
  const [usuarioId, setUsuarioId] = useState("");
  const [usuarioNome, setUsuarioNome] = useState("");

  const handleAdd = () => {
    if (!nome || !objetivo || !usuarioId || !usuarioNome) return;
    const novoPlano: PlanoTreino = {
      id: planos.length + 1,
      nome,
      objetivo,
      usuario: { id: Number(usuarioId), nome: usuarioNome },
    };
    setPlanos([novoPlano, ...planos]);
    setNome("");
    setObjetivo("");
    setUsuarioId("");
    setUsuarioNome("");
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Planos de Treino</Text>
      <TextInput
        style={styles.input}
        placeholder="Nome do Plano"
        value={nome}
        onChangeText={setNome}
      />
      <TextInput
        style={styles.input}
        placeholder="Objetivo"
        value={objetivo}
        onChangeText={setObjetivo}
      />
      <TextInput
        style={styles.input}
        placeholder="ID do Usuário"
        value={usuarioId}
        onChangeText={setUsuarioId}
        keyboardType="numeric"
      />
      <TextInput
        style={styles.input}
        placeholder="Nome do Usuário"
        value={usuarioNome}
        onChangeText={setUsuarioNome}
      />
      <Button title="Adicionar Plano" onPress={handleAdd} />
      <FlatList
        data={planos}
        keyExtractor={(item) => item.id.toString()}
        renderItem={({ item }) => (
          <View style={styles.item}>
            <Text style={styles.itemTitle}>{item.nome}</Text>
            <Text>Objetivo: {item.objetivo}</Text>
            <Text>Usuário: {item.usuario.nome}</Text>
          </View>
        )}
      />
      {planos.length === 0 && (
        <Text style={styles.mensagem}>Nenhum plano de treino encontrado.</Text>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, padding: 20, backgroundColor: Colors.light.background },
  title: { fontSize: 24, fontWeight: "bold", marginBottom: 20 },
  input: {
    borderWidth: 1,
    borderColor: "#ccc",
    borderRadius: 5,
    padding: 10,
    marginBottom: 10,
  },
  item: {
    padding: 15,
    borderBottomWidth: 1,
    borderBottomColor: Colors.light.text,
    backgroundColor: Colors.light.background,
  },
  itemTitle: { fontWeight: "bold", fontSize: 18 },
  mensagem: { marginTop: 20, color: Colors.light.tint, fontWeight: "bold" },
});

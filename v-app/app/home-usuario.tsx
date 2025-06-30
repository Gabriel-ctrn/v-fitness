import React, { useState, useEffect } from "react";
import {
  View,
  Text,
  Button,
  FlatList,
  StyleSheet,
  TouchableOpacity,
  TextInput,
  Modal,
} from "react-native";
import { useNavigation } from "@react-navigation/native";
import { Colors } from "../constants/Colors";

type Treino = {
  id: number;
  nome: string;
  // Adicione outros campos se necessário
};

export default function HomeUsuario() {
  // Substituir mock por estado real
  const usuarioId = 1; // TODO: trocar pelo id real do usuário logado
  const [treinos, setTreinos] = useState<Treino[]>([]);
  const [mensagem, setMensagem] = useState("");
  const [novoTreino, setNovoTreino] = useState("");
  const [loading, setLoading] = useState(false);
  const [treinoSelecionado, setTreinoSelecionado] = useState<Treino | null>(
    null
  );
  const [modalVisible, setModalVisible] = useState(false);

  const navigation = useNavigation();

  useEffect(() => {
    buscarTreinos();
  }, []);

  const buscarTreinos = async () => {
    setLoading(true);
    try {
      const resp = await fetch(
        `http://localhost:8080/treinos/usuario/${usuarioId}`
      );
      if (!resp.ok) throw new Error("Erro ao buscar treinos");
      const data = await resp.json();
      setTreinos(data);
    } catch (e) {
      setMensagem("Erro ao buscar treinos");
    } finally {
      setLoading(false);
    }
  };

  const handleIniciarTreino = (treinoNome: string) => {
    setMensagem(`Treino "${treinoNome}" iniciado!`);
    setTimeout(() => setMensagem(""), 2000);
  };

  const handleAdicionarTreino = async () => {
    if (!novoTreino.trim()) return;
    setLoading(true);
    try {
      const resp = await fetch("http://localhost:8080/treinos", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "X-Perfil": "ALUNO",
        },
        body: JSON.stringify({
          nome: novoTreino,
          usuario: { id: usuarioId },
          // Adicione outros campos obrigatórios do Treino se necessário
        }),
      });
      if (!resp.ok) throw new Error("Erro ao adicionar treino");
      setMensagem("Treino adicionado!");
      setNovoTreino("");
      buscarTreinos();
    } catch (e) {
      setMensagem("Erro ao adicionar treino");
    } finally {
      setLoading(false);
    }
  };

  const handleTreinoPress = (treino: Treino) => {
    setTreinoSelecionado(treino);
    setModalVisible(true);
  };

  const handleEditarExercicios = () => {
    setModalVisible(false);
    if (treinoSelecionado) {
      navigation.navigate("exercicios-treino", {
        treinoId: treinoSelecionado.id,
      });
    }
  };

  const handleIniciarTreinoModal = () => {
    setModalVisible(false);
    if (treinoSelecionado) {
      navigation.navigate("fluxo-treino", {
        treinoId: treinoSelecionado.id,
        treinoNome: treinoSelecionado.nome,
      });
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Bem-vindo ao V-Fitness!</Text>
      <Text style={styles.subtitle}>Seus Treinos:</Text>
      {loading && <Text>Carregando...</Text>}
      <FlatList
        data={treinos}
        keyExtractor={(item: Treino) => item.id.toString()}
        renderItem={({ item }: { item: Treino }) => (
          <TouchableOpacity
            style={styles.treinoBtn}
            onPress={() => handleTreinoPress(item)}
          >
            <Text style={styles.treinoBtnText}>{item.nome}</Text>
          </TouchableOpacity>
        )}
      />
      <Modal
        visible={modalVisible}
        transparent
        animationType="slide"
        onRequestClose={() => setModalVisible(false)}
      >
        <View
          style={{
            flex: 1,
            justifyContent: "center",
            alignItems: "center",
            backgroundColor: "rgba(0,0,0,0.3)",
          }}
        >
          <View
            style={{
              backgroundColor: "#fff",
              padding: 24,
              borderRadius: 8,
              minWidth: 250,
            }}
          >
            <Text
              style={{ fontWeight: "bold", fontSize: 18, marginBottom: 16 }}
            >
              O que deseja fazer?
            </Text>
            <Button
              title="Editar Exercícios"
              onPress={handleEditarExercicios}
            />
            <View style={{ height: 12 }} />
            <Button title="Iniciar Treino" onPress={handleIniciarTreinoModal} />
            <View style={{ height: 12 }} />
            <Button
              title="Cancelar"
              color="#888"
              onPress={() => setModalVisible(false)}
            />
          </View>
        </View>
      </Modal>
      <View style={styles.addTreinoBox}>
        <TextInput
          style={styles.input}
          placeholder="Novo treino (ex: Abdômen)"
          value={novoTreino}
          onChangeText={setNovoTreino}
        />
        <Button
          title="Adicionar Treino"
          onPress={handleAdicionarTreino}
          disabled={loading}
        />
      </View>
      {mensagem ? <Text style={styles.mensagem}>{mensagem}</Text> : null}
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, padding: 20, backgroundColor: Colors.light.background },
  card: {
    backgroundColor: Colors.light.background,
    borderRadius: 10,
    padding: 16,
    marginBottom: 16,
    shadowColor: Colors.light.text,
    shadowOpacity: 0.1,
    shadowRadius: 8,
    elevation: 2,
  },
  title: {
    fontSize: 28,
    fontWeight: "bold",
    marginBottom: 20,
    textAlign: "center",
    color: Colors.light.text,
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
    backgroundColor: Colors.light.tint,
    borderRadius: 8,
    padding: 12,
    marginTop: 10,
  },
  treinoBtnText: {
    color: Colors.light.background,
    fontWeight: "bold",
    textAlign: "center",
  },
  mensagem: {
    marginTop: 20,
    color: Colors.light.tint,
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

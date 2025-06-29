import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  Button,
  StyleSheet,
  TouchableOpacity,
} from "react-native";
import { useRouter } from "expo-router";

export default function LoginScreen() {
  const router = useRouter();
  const [isCadastro, setIsCadastro] = useState(false);
  const [tipo, setTipo] = useState<"usuario" | "academia">("usuario");
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [nome, setNome] = useState("");
  const [endereco, setEndereco] = useState("");
  const [perfil, setPerfil] = useState<"ALUNO" | "ADMIN">("ALUNO");
  const [academiaId, setAcademiaId] = useState("1");
  const [loading, setLoading] = useState(false);
  const [mensagem, setMensagem] = useState("");

  const handleLogin = async () => {
    setLoading(true);
    setMensagem("");
    try {
      const res = await fetch(
        `http://localhost:8080/${
          tipo === "usuario" ? "usuarios/login" : "academias/login"
        }`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ email, senha }),
        }
      );
      if (res.ok) {
        setMensagem("Login realizado com sucesso!");
        if (tipo === "usuario") {
          setTimeout(() => router.replace("/home-usuario"), 1000);
        }
      } else {
        setMensagem("Usuário ou senha inválidos.");
      }
    } catch (e) {
      setMensagem("Erro de conexão com o servidor.");
    }
    setLoading(false);
  };

  const handleCadastro = async () => {
    setLoading(true);
    setMensagem("");
    try {
      if (tipo === "usuario") {
        const res = await fetch("http://localhost:8080/usuarios", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            nome,
            email,
            senha,
            nivelExperiencia: "",
            perfil,
            academiaId: Number(academiaId),
          }),
        });
        setMensagem(
          res.ok ? "Cadastro realizado!" : "Erro ao cadastrar usuário."
        );
      } else {
        const res = await fetch("http://localhost:8080/academias", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ nome, endereco, email, senha }),
        });
        setMensagem(
          res.ok ? "Cadastro realizado!" : "Erro ao cadastrar academia."
        );
      }
    } catch (e) {
      setMensagem("Erro de conexão com o servidor.");
    }
    setLoading(false);
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>V-Fitness</Text>
      <View style={styles.switchContainer}>
        <TouchableOpacity
          onPress={() => setTipo("usuario")}
          style={[
            styles.switchButton,
            tipo === "usuario" && styles.switchButtonActive,
          ]}
        >
          <Text style={styles.switchText}>Usuário</Text>
        </TouchableOpacity>
        <TouchableOpacity
          onPress={() => setTipo("academia")}
          style={[
            styles.switchButton,
            tipo === "academia" && styles.switchButtonActive,
          ]}
        >
          <Text style={styles.switchText}>Academia</Text>
        </TouchableOpacity>
      </View>
      {isCadastro ? (
        <>
          <TextInput
            style={styles.input}
            placeholder={
              tipo === "usuario" ? "Nome do Usuário" : "Nome da Academia"
            }
            value={nome}
            onChangeText={setNome}
          />
          {tipo === "academia" && (
            <>
              <TextInput
                style={styles.input}
                placeholder="Endereço da Academia"
                value={endereco}
                onChangeText={setEndereco}
              />
              <TextInput
                style={styles.input}
                placeholder="Email da Academia"
                value={email}
                onChangeText={setEmail}
                keyboardType="email-address"
              />
              <TextInput
                style={styles.input}
                placeholder="Senha da Academia"
                value={senha}
                onChangeText={setSenha}
                secureTextEntry
              />
            </>
          )}
          {tipo === "usuario" && (
            <>
              <TextInput
                style={styles.input}
                placeholder="Email"
                value={email}
                onChangeText={setEmail}
                keyboardType="email-address"
              />
              <TextInput
                style={styles.input}
                placeholder="Perfil (ALUNO ou ADMIN)"
                value={perfil}
                onChangeText={(text) =>
                  setPerfil(text === "ADMIN" ? "ADMIN" : "ALUNO")
                }
              />
              <TextInput
                style={styles.input}
                placeholder="ID da Academia"
                value={academiaId}
                onChangeText={setAcademiaId}
                keyboardType="numeric"
              />
            </>
          )}
          <TextInput
            style={styles.input}
            placeholder="Senha"
            value={senha}
            onChangeText={setSenha}
            secureTextEntry
          />
          <Button
            title={loading ? "Cadastrando..." : "Cadastrar"}
            onPress={handleCadastro}
            disabled={loading}
          />
          <TouchableOpacity
            onPress={() => setIsCadastro(false)}
            style={styles.link}
          >
            <Text>Já tem conta? Entrar</Text>
          </TouchableOpacity>
        </>
      ) : (
        <>
          {tipo === "usuario" && (
            <TextInput
              style={styles.input}
              placeholder="Email"
              value={email}
              onChangeText={setEmail}
              keyboardType="email-address"
            />
          )}
          <TextInput
            style={styles.input}
            placeholder="Senha"
            value={senha}
            onChangeText={setSenha}
            secureTextEntry
          />
          <Button
            title={loading ? "Entrando..." : "Entrar"}
            onPress={handleLogin}
            disabled={loading}
          />
          <TouchableOpacity
            onPress={() => setIsCadastro(true)}
            style={styles.link}
          >
            <Text>Não tem conta? Cadastre-se</Text>
          </TouchableOpacity>
        </>
      )}
      {!!mensagem && <Text style={styles.mensagem}>{mensagem}</Text>}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    padding: 20,
    backgroundColor: "#fff",
  },
  title: { fontSize: 32, fontWeight: "bold", marginBottom: 30 },
  input: {
    borderWidth: 1,
    borderColor: "#ccc",
    borderRadius: 5,
    padding: 10,
    marginBottom: 10,
    width: 250,
  },
  switchContainer: { flexDirection: "row", marginBottom: 20 },
  switchButton: {
    padding: 10,
    borderWidth: 1,
    borderColor: "#ccc",
    borderRadius: 5,
    marginHorizontal: 5,
  },
  switchButtonActive: { backgroundColor: "#A1CEDC" },
  switchText: { fontWeight: "bold" },
  link: { marginTop: 10 },
  mensagem: { marginTop: 20, color: "#007700", fontWeight: "bold" },
});

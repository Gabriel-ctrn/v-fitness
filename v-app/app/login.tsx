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
import { Colors } from "../constants/Colors";
import { Image } from "expo-image";

export default function LoginScreen() {
  const router = useRouter();
  const [isCadastro, setIsCadastro] = useState(false);
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [nome, setNome] = useState("");
  const [perfil, setPerfil] = useState<"ALUNO" | "ADMIN">("ALUNO");
  const [loading, setLoading] = useState(false);
  const [mensagem, setMensagem] = useState("");

  const handleLogin = async () => {
    setLoading(true);
    setMensagem("");
    try {
      const res = await fetch(`http://localhost:8080/usuarios/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, senha }),
      });
      if (res.ok) {
        setMensagem("Login realizado com sucesso!");
        setTimeout(() => router.replace("/home-usuario"), 1000);
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
      const res = await fetch("http://localhost:8080/usuarios", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          nome,
          email,
          senha,
          nivelExperiencia: "",
          perfil,
        }),
      });
      setMensagem(
        res.ok ? "Cadastro realizado!" : "Erro ao cadastrar usuário."
      );
    } catch (e) {
      setMensagem("Erro de conexão com o servidor.");
    }
    setLoading(false);
  };

  return (
    <View style={styles.container}>
      <Image source={require("@/assets/images/logo.png")} style={styles.logo} />
      <Text style={styles.title}>V-Fitness</Text>
      {isCadastro ? (
        <>
          <TextInput
            style={styles.input}
            placeholder="Nome do Usuário"
            value={nome}
            onChangeText={setNome}
          />
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
          <TextInput
            style={styles.input}
            placeholder="Email"
            value={email}
            onChangeText={setEmail}
            keyboardType="email-address"
          />
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
    backgroundColor: Colors.light.background,
  },
  title: {
    fontSize: 32,
    fontWeight: "bold",
    marginBottom: 30,
    color: Colors.light.text,
  },
  input: {
    borderWidth: 1,
    borderColor: Colors.light.text,
    borderRadius: 5,
    padding: 10,
    marginBottom: 10,
    width: 250,
    color: Colors.light.text,
    backgroundColor: Colors.light.background,
  },
  link: { marginTop: 10, color: Colors.light.tint },
  mensagem: { marginTop: 20, color: Colors.light.tint, fontWeight: "bold" },
  logo: {
    width: 180,
    height: 120,
    marginBottom: 12,
    resizeMode: "contain",
  },
});

import { Image } from "expo-image";
import { StyleSheet, View, Text, TouchableOpacity } from "react-native";
import { useRouter } from "expo-router";
import { Colors } from "../../constants/Colors";

export default function HomeScreen() {
  const router = useRouter();
  return (
    <View style={styles.container}>
      <View style={styles.headerBox}>
        <Image
          source={require("@/assets/images/logo.png")}
          style={styles.logo}
        />
        <Text style={styles.titulo}>V-Fitness</Text>
        <Text style={styles.subtitulo}>Seu treino inteligente com IA</Text>
      </View>
      <View style={styles.iaBox}>
        <Text style={styles.iaText}>
          Receba sugestões personalizadas de exercícios, ajuste seu treino em
          tempo real e evolua com o apoio da inteligência artificial Gemini.
        </Text>
      </View>
      <TouchableOpacity
        style={styles.botao}
        onPress={() => router.push("/login")}
      >
        <Text style={styles.botaoText}>Entrar</Text>
      </TouchableOpacity>
      <TouchableOpacity
        style={[styles.botao, styles.botaoSecundario]}
        onPress={() => router.push("/login?cadastro=1")}
      >
        <Text style={[styles.botaoText, { color: Colors.light.tint }]}>
          Cadastrar
        </Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.light.background,
    alignItems: "center",
    justifyContent: "center",
    padding: 24,
  },
  headerBox: {
    alignItems: "center",
    marginBottom: 32,
  },
  logo: {
    width: 180,
    height: 120,
    marginBottom: 12,
    resizeMode: "contain",
  },
  titulo: {
    fontSize: 32,
    fontWeight: "bold",
    color: Colors.light.text,
    marginBottom: 4,
  },
  subtitulo: {
    fontSize: 16,
    color: Colors.light.tint,
    marginBottom: 12,
    fontWeight: "600",
  },
  iaBox: {
    backgroundColor: Colors.light.tint,
    borderRadius: 12,
    padding: 18,
    marginBottom: 32,
    width: "100%",
    shadowColor: Colors.light.text,
    shadowOpacity: 0.08,
    shadowRadius: 8,
    elevation: 2,
  },
  iaText: {
    color: Colors.light.background,
    fontSize: 16,
    textAlign: "center",
    fontWeight: "500",
  },
  botao: {
    backgroundColor: Colors.light.text,
    borderRadius: 8,
    paddingVertical: 14,
    paddingHorizontal: 32,
    marginBottom: 16,
    width: "100%",
    alignItems: "center",
  },
  botaoSecundario: {
    backgroundColor: Colors.light.background,
    borderWidth: 2,
    borderColor: Colors.light.tint,
  },
  botaoText: {
    color: Colors.light.background,
    fontWeight: "bold",
    fontSize: 18,
  },
});

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
        <Text style={styles.botaoText}>Começar</Text>
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
    fontSize: 36,
    fontWeight: "bold",
    color: Colors.light.text,
    marginBottom: 6,
  },
  subtitulo: {
    fontSize: 18,
    color: Colors.light.tint,
    fontWeight: "600",
  },
  iaBox: {
    backgroundColor: Colors.light.tint,
    borderRadius: 12,
    padding: 18,
    marginBottom: 32,
    width: "100%",
    shadowColor: "#000",
    shadowOpacity: 0.1,
    shadowRadius: 6,
    shadowOffset: { width: 0, height: 2 },
    elevation: 3,
  },
  iaText: {
    color: Colors.light.background,
    fontSize: 16,
    lineHeight: 24,
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
  botaoText: {
    color: Colors.light.background,
    fontWeight: "bold",
    fontSize: 18,
  },
  botaoSecundario: {
    backgroundColor: Colors.light.background,
    borderWidth: 2,
    borderColor: Colors.light.tint,
  },
  botaoTextSecundario: {
    color: Colors.light.tint,
  },
});

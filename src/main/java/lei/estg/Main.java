package lei.estg;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import lei.estg.dataStructures.ArrayOrderedList;
import lei.estg.dataStructures.exceptions.EmptyStackException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * The main method that serves as the entry point for the application.
     * This method prints a welcome message to the console and then calls the menu method
     * to display the main menu and handle user input.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        System.out.println("Bem vindo ao jogo!");
        menu();
    }

    /**
     * Displays the main menu and handles user input for menu options.
     * This method presents the user with a menu of options, reads the user's choice,
     * and executes the corresponding action. The menu options include:
     * 1 - Start the game
     * 2 - View reports
     * 3 - Configure settings
     * 4 - Exit the application
     * The method continues to display the menu until the user chooses to exit.
     */
    private static void menu() {

        int opcao = 0;
        do {
            System.out.println("Selecione uma opção:");
            System.out.println("1 - Jogar");
            System.out.println("2 - Ver relatorios");
            System.out.println("3 - Configurações");
            System.out.println("4 - Sair");
            System.out.println("Opção: ");

            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
            } else {
                scanner.next();
            }

            switch (opcao) {
                case 1:
                    System.out.println("Você escolheu jogar!");
                    jogar();
                    break;
                case 2:
                    System.out.println("Você escolheu ver relatórios!");
                    try {
                        mostrarRelatorios("relatorioMissoes.json");
                    } catch (EmptyStackException e) {
                        System.out.println("Erro ao mostrar relatórios: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Você escolheu configurar!");
                    try {
                        Path caminhoConfig = Paths
                                .get(Objects.requireNonNull(Main.class.getClassLoader().getResource("config/simuladorConfig.json")).toURI());
                        editarConfiguracoesJogo(String.valueOf(caminhoConfig.toString()));
                    } catch (Exception e) {
                        System.out.println("Erro ao carregar o arquivo de configurações: " + e.getMessage());
                    }
                    break;
                case 4:
                    System.out.println("Até breve!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        } while (opcao < 1 || opcao > 4);
    }

    /**
     * Starts the game and allows the user to select a game mode.
     * This method displays a menu for the user to choose between manual simulation,
     * automatic simulation, or returning to the main menu. Based on the user's choice,
     * it either starts the selected simulation mode or returns to the main menu.
     */
    private static void jogar() {
        int opcao = 0;

        do {
            System.out.println("Selecione um modo de Jogo:");
            System.out.println("1 - Simulação Manual");
            System.out.println("2 - Simulação Automática");
            System.out.println("3 - Voltar");
            System.out.println("Opção: ");

            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
            } else {
                scanner.next();
            }
            switch (opcao) {
                case 1:
                    System.out.println("Você escolheu simulação manual!");
                    SimulacaoManual.jogar();
                    break;
                case 2:
                    System.out.println("Você escolheu simulação automática!");
                    SimulacaoAutomatica.jogar();
                    break;
                case 3:
                    System.out.println("Voltando ao menu...");
                    menu();
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        } while (opcao != 3);
    }

    /**
     * Displays the mission reports from a JSON file.
     * This method reads the mission reports from the specified JSON file,
     * deserializes the data, and prints the mission details and attempts to the console.
     * If the file is empty or an error occurs during reading, it initializes an empty report structure.
     *
     * @param caminhoFicheiro the path to the JSON file containing the mission reports
     * @throws EmptyStackException if an error occurs while processing the mission reports
     */
    private static void mostrarRelatorios(String caminhoFicheiro) throws EmptyStackException {

        JsonObject root;
        try (FileReader reader = new FileReader(caminhoFicheiro)) {
            root = (JsonObject) Jsoner.deserialize(reader);
        } catch (Exception e) {
            root = new JsonObject();
            root.put("missoes", new JsonArray());
        }

        JsonArray missoes = (JsonArray) root.get("missoes");

        for (Object objMissao : missoes) {
            JsonObject missao = (JsonObject) objMissao;
            JsonArray tentativas = (JsonArray) missao.get("tentativas");

            ArrayOrderedList<TentativaComparable> orderedTentativas = new ArrayOrderedList<>();

            for (Object objTentativa : tentativas) {
                JsonObject tentativa = (JsonObject) objTentativa;

                orderedTentativas.add(new TentativaComparable(tentativa));
            }

            System.out.println("Missão: " + missao.get("codMissao") + " - Versão: " + missao.get("versao") + " - Dificuldade: " + missao.get("dificuldade"));
            Iterator itTentativas = orderedTentativas.iterator();
            while (itTentativas.hasNext()) {
                TentativaComparable tentativa = (TentativaComparable) itTentativas.next();
                System.out.println("Tentativa: " + tentativa.getTentativa().get("codTentativa") + " - Vida: " + tentativa.getTentativa().get("vida"));
            }
        }
        menu();
    }

    /**
     * Edits the game configurations from a JSON file.
     * This method reads the game configurations from the specified JSON file,
     * allows the user to edit the player settings, and saves the updated configurations back to the file.
     * If the file cannot be read or the player settings are not found, appropriate error messages are displayed.
     *
     * @param caminhoFicheiro the path to the JSON file containing the game configurations
     */
    private static void editarConfiguracoesJogo(String caminhoFicheiro) {
        JsonObject root;

        try (FileReader reader = new FileReader(caminhoFicheiro)) {
            root = (JsonObject) Jsoner.deserialize(reader);
        } catch (Exception e) {
            System.out.println("Erro ao carregar o arquivo de configurações: " + e.getMessage());
            return;
        }

        JsonObject player = (JsonObject) root.get("player");
        if (player == null) {
            System.out.println("Configurações do jogador não encontradas.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("Editar configurações do jogador:");

        System.out.print("Nome atual (" + player.get("nome") + "): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.isBlank()) {
            player.put("nome", novoNome);
        }

        System.out.print("Mochila limite atual (" + player.get("mochila-limite") + "): ");
        String novaMochilaLimite = scanner.nextLine();
        if (!novaMochilaLimite.isBlank()) {
            try {
                player.put("mochila-limite", Integer.parseInt(novaMochilaLimite));
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido para mochila limite. Não foi alterado.");
            }
        }

        System.out.print("Vida máxima atual (" + player.get("vidaMaxima") + "): ");
        String novaVidaMaxima = scanner.nextLine();
        if (!novaVidaMaxima.isBlank()) {
            try {
                player.put("vidaMaxima", Integer.parseInt(novaVidaMaxima));
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido para vida máxima. Não foi alterado.");
            }
        }

        System.out.print("Poder atual (" + player.get("poder") + "): ");
        String novoPoder = scanner.nextLine();
        if (!novoPoder.isBlank()) {
            try {
                player.put("poder", Integer.parseInt(novoPoder));
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido para poder. Não foi alterado.");
            }
        }

        try (FileWriter writer = new FileWriter(caminhoFicheiro)) {
            Jsoner.serialize(root, writer);
            System.out.println("Configurações salvas com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo de configurações: " + e.getMessage());
        }
        menu();
    }

    /**
     * A class that represents a comparable attempt in the game.
     * This class implements the Comparable interface to allow sorting of attempts
     * based on the player's life value.
     */
    static class TentativaComparable implements Comparable<TentativaComparable> {
        private final JsonObject tentativa;

        public TentativaComparable(JsonObject tentativa) {
            this.tentativa = tentativa;
        }

        public JsonObject getTentativa() {
            return tentativa;
        }


        /**
         * Compares this attempt with another attempt based on the player's life value.
         *
         * @param other the other TentativaComparable object to compare to
         * @return a negative integer, zero, or a positive integer as this attempt's life value
         *         is less than, equal to, or greater than the other attempt's life value
         */
        @Override
        public int compareTo(TentativaComparable other) {
            int vidaThis = ((Number) this.tentativa.get("vida")).intValue();
            int vidaOther = ((Number) other.tentativa.get("vida")).intValue();
            return Integer.compare(vidaThis, vidaOther);
        }
    }
}

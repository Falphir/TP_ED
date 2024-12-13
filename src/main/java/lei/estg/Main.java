package lei.estg;

import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

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
        System.out.println("Selecione uma opção:");
        System.out.println("1 - Jogar");
        System.out.println("2 - Ver relatorios");
        System.out.println("3 - Configurações");
        System.out.println("4 - Sair");
        System.out.println("Opção: ");

        int opcao = scanner.nextInt();

        while (opcao != 4) {
            switch (opcao) {
                case 1:
                    System.out.println("Você escolheu jogar!");
                    jogar();
                    break;
                case 2:
                    System.out.println("Você escolheu ver relatórios!");
                    break;
                case 3:
                    System.out.println("Você escolheu configurar!");
                    break;
                case 4:
                    System.out.println("Até breve!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    /**
     * Starts the game and allows the user to select a game mode.
     * This method displays a menu for the user to choose between manual simulation,
     * automatic simulation, or returning to the main menu. Based on the user's choice,
     * it either starts the selected simulation mode or returns to the main menu.
     */
    private static void jogar() {
        System.out.println("Selecione um modo de Jogo:");
        System.out.println("1 - Simulação Manual");
        System.out.println("2 - Simulação Automática");
        System.out.println("3 - Voltar");
        System.out.println("Opção: ");

        int opcao = scanner.nextInt();

        while (opcao != 3) {
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
        }
    }
}

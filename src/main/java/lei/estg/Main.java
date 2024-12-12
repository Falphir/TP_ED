package lei.estg;

import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Bem vindo ao jogo!");
        menu();
    }

    public static void menu() {
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

    public static void jogar() {
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

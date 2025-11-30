import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static List<Conta> contas = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        int opcao;

        do {
            System.out.println("=============================================================================");
            System.out.println(" MENU BANCÁRIO - BANCO PAÇOCA");
            System.out.println("=============================================================================");
            System.out.println("1. Criar Conta");
            System.out.println("2. Depositar");
            System.out.println("3. Sacar");
            System.out.println("4. Transferir");
            System.out.println("5. Pagar Boleto");
            System.out.println("6. Recarga de Celular");
            System.out.println("7. Ver saldo");
            System.out.println("8. Imprimir extrato");
            System.out.println("9. Listar Contas");
            System.out.println("10. Consulta Cheque Especial");
            System.out.println("0. Sair");
            System.out.println("Escolhar uma opção: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1 -> criarConta();
                case 2 -> depositar();
                case 3 -> sacar();
                case 4 -> transferir();
                case 5 -> pagarBoleto();
                case 6 -> recarregarCelular();
                case 7 -> verSaldo();
                case 8 -> extrato();
                case 9 -> listarContas();
                case 10 -> consultarChequeEspecial();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }



    public static void criarConta() {
        scanner.nextLine();
        System.out.println("Nome do Cliente: ");
        String nome = scanner.nextLine();

        Cliente cliente = new Cliente();
        cliente.setNome(nome);

        System.out.println("Tipo de Conta:");
        System.out.println("1- Conta Corrente");
        System.out.println("2- Conta Poupança");
        int tipo = scanner.nextInt();

        Conta conta;

        if (tipo == 1)
            conta = new ContaCorrente(cliente);
        else if (tipo == 2)
            conta = new ContaPoupanca(cliente);
        else {
            System.out.println("Tipo inválido.");
            return;
        }

        contas.add(conta);
        System.out.println(" Conta criada com sucesso! Número: " + conta.getNumero());
    }

    public static Conta buscarConta(int numero) {
        return contas.stream()
                .filter(c -> c.getNumero() == numero)
                .findFirst()
                .orElse(null);
    }

    public static void depositar() {
        System.out.println("Número da conta: ");
        int numero = scanner.nextInt();
        Conta conta = buscarConta(numero);

        if (conta == null) {
            System.out.println("Conta não encontrada.");
            return;
        }

        System.out.println("Valor do depósito: ");
        double valor = scanner.nextDouble();
        conta.depositar(valor);
    }

    public static void sacar() {
        System.out.println("Número da conta: ");
        int numero = scanner.nextInt();
        Conta conta = buscarConta(numero);


        if (conta == null) {
            System.out.println("Conta não encontrada.");
            return;
        }

        System.out.println("Valor do Saque: ");
        double valor = scanner.nextDouble();
        conta.sacar(valor);
    }

    public static void transferir() {
        System.out.println("Conta origem: ");
        int origemNum = scanner.nextInt();
        Conta origem = buscarConta(origemNum);

        if (origem == null) {
            System.out.println("Conta de origem não encontrada.");
            return;
        }

        System.out.println("Conta destino: ");
        int destinoNum = scanner.nextInt();
        Conta destino = buscarConta(destinoNum);

        if (destino == null) {
            System.out.println("Conta destino não encontrada.");
            return;
        }

        System.out.println("Valor da transferência: ");
        double valor = scanner.nextDouble();

        origem.transferir(valor, destino);
    }

    public static void pagarBoleto() {
        System.out.println("Número da conta: ");
        int numero = scanner.nextInt();
        Conta conta = buscarConta(numero);

        if (conta == null) {
            System.out.println("Conta não encontrada.");
            return;
        }

        scanner.nextLine();
        System.out.println("Descrição do boleto: ");
        String desc = scanner.nextLine();

        System.out.println("Valor do boleto: ");
        double valor = scanner.nextDouble();

        conta.pagarBoleto(valor, desc);
    }

    public static void recarregarCelular() {
        System.out.println("Número da conta: ");
        int numero = scanner.nextInt();
        Conta conta = buscarConta(numero);

        if (conta == null) {
            System.out.println("Conta não encontrada.");
            return;
        }

        scanner.nextLine();
        System.out.println("Número do Celular (DDD + número): ");
        String cel = scanner.nextLine();

        System.out.println("Valor da recarga: ");
        double valor = scanner.nextDouble();

        conta.recarregarCelular(cel, valor);
    }

    public static void verSaldo() {
        System.out.println("Número da conta: ");
        int numero = scanner.nextInt();
        Conta conta = buscarConta(numero);

        if (conta == null) {
            System.out.println("Conta não encontrada.");
            return;
        }

        conta.verificarSaldo();
    }

    public static void extrato() {
        System.out.println("Número da conta: ");
        int numero = scanner.nextInt();
        Conta conta = buscarConta(numero);

        if (conta == null) {
            System.out.println("Conta não encontrada.");
            return;
        }

        conta.imprimirExtrato();
    }
    public static void listarContas() {
        System.out.println("\n==== LISTA DE CONTAS ====");
        for (Conta c : contas ) {
            System.out.printf(
                    "Cliente: %s | Conta: %d | Agência: %d | Saldo: %s | Tipo de Conta: %s\n",
                    c.getTitular(),
                    c.getNumero(),
                    c.getAgencia(),
                    c.formatarValor(c.getSaldoReal()),   // << aqui trocou
                    c.getTipoConta()
            );
        }
    }



    public static void consultarChequeEspecial() {
        System.out.println("Número da conta: ");
        int numero = scanner.nextInt();
        Conta conta = buscarConta(numero);

        if (conta == null) {
            System.out.println("Conta não encontrada.");
            return;
        }

        conta.consultarChequeEspecial();
    }

}
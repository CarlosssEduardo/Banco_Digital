
public class ContaCorrente extends Conta {

    // quando você escreve Cliente cliente, está dizendo:
    //"Esse construtor recebe um objeto do tipo Cliente e vou chama lo de cliente dentro do construtor."
    public ContaCorrente(Cliente cliente) {
        // O super(cliente) repassa esse objeto para o construtor da classe mãe (Conta), que guarda esse cliente como dono da conta.
        super(cliente);
    }

    @Override
    public String getTipoConta() {
        return "Conta Corrente";
    }


    @Override
    public void imprimirExtrato() {
        System.out.println("=== Extrato Conta Corrente ===");

        // imprime informações que você já tinha antes
        super.imprimirInfosComuns();

        // extrato detalhado
        System.out.println("\n--- Movimentações ---");

        if (extrato.isEmpty()) {
            System.out.println("Nenhuma movimentação encontrada.");
        } else {
            for (Operacao op : extrato) {
                System.out.printf("%s: R$ %.2f\n", op.descricao(), op.valor());
            }
        }

        System.out.println("--------------------------");
    }


}
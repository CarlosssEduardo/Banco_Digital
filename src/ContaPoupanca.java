
public class ContaPoupanca extends Conta {

    public ContaPoupanca(Cliente cliente) {
        super(cliente);
    }
    @Override
    public String getTipoConta() {
        return "Conta Poupança";
    }


    @Override
    public void imprimirExtrato() {
        System.out.println("=== Extrato Conta Poupança ===");
        super.imprimirInfosComuns();

        System.out.println("\n--- Movimentações ---");

        if (extrato.isEmpty()) {
            System.out.println("Nenhuma movimentação registrada.");
        } else {
            for (Operacao op : extrato) {
                System.out.println(op);
            }
        }
    }

}
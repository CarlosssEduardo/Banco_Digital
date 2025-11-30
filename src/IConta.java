public interface IConta {

    void sacar(double valor);

    void depositar(double valor);

    void transferir(double valor, IConta contaDestino);

    void imprimirExtrato();

    void recarregarCelular(String cel, double valor);

    void depositarSilencioso(double valor);

    String getTitular();
}
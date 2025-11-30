// Classe abstrata: serve como modelo base para ContaCorrente e ContaPoupanca

import java.util.ArrayList;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

public abstract class Conta implements IConta {

    // Formatador de dinheiro
    protected String formatarValor(double valor) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return nf.format(valor);
    }

    // Lista que registra todas as operações feitas (extrato)
    protected List<Operacao> extrato = new ArrayList<>();

    protected void registrarOperacao(String descricao, double valor) {
        extrato.add(new Operacao(descricao, valor));
    }


    private static final int AGENCIA_PADRAO = 1;
    private static int SEQUENCIAL = 1;

    protected int agencia;
    protected int numero;
    protected double saldo;

    // Sistema de Limite (Cheque Especial)
    protected double ChequeEspecial = 500;
    protected double ChequeEspecialUsado = 0;

    // Dono da conta
    protected Cliente cliente;

    // Construtor
    public Conta(Cliente cliente) {
        this.agencia = AGENCIA_PADRAO;
        this.numero = SEQUENCIAL++;
        this.cliente = cliente;
    }

    // ============================================================
    //                       MÉTODOS PRINCIPAIS
    // ============================================================

    @Override
    public void sacar(double valor) {

        if (valor <= saldo) {
            saldo -= valor;
            registrarOperacao("Saque", valor);
            System.out.println(
                    "\n💸 Saque efetuado com sucesso!\n" +
                            "Valor sacado: " + formatarValor(valor));
            return;
        }

        double disponivel = saldo + (ChequeEspecial - ChequeEspecialUsado);

        if (valor > disponivel) {
            System.out.println("Saque negado! Saldo + Limite insuficiente.");
            return;
        }

        double restante = valor - saldo;
        saldo = 0;
        ChequeEspecialUsado += restante;

        registrarOperacao("Saque usando Cheque Especial", valor);
        registrarOperacao("Uso do Cheque Especial", restante);

        System.out.println(
                "\n⚠️ Saque realizado utilizando o Cheque Especial.\n" +
                        "Valor debitado do limite: " + formatarValor(restante)
        );

    }

    protected boolean sacarSilencioso(double valor) {

        if (valor <= saldo) {
            saldo -= valor;
            return true;
        }

        double disponivel = saldo + (ChequeEspecial - ChequeEspecialUsado);

        if (valor > disponivel) {
            return false;
        }

        double restante = valor - saldo;
        saldo = 0;
        ChequeEspecialUsado += restante;

        return true;
    }

    public void depositarSilencioso(double valor) {

        if (ChequeEspecialUsado > 0) {
            double diferenca = ChequeEspecialUsado - valor;

            if (diferenca <= 0) {
                ChequeEspecialUsado = 0;
                saldo += Math.abs(diferenca);
            } else {
                ChequeEspecialUsado = diferenca;
                return;
            }

        } else {
            saldo += valor;
        }

    }

    @Override
    public void depositar(double valor) {

        if (ChequeEspecialUsado > 0) {
            double diferenca = ChequeEspecialUsado - valor;

            if (diferenca <= 0) {
                ChequeEspecialUsado = 0;
                saldo += Math.abs(diferenca);
            } else {
                ChequeEspecialUsado = diferenca;
                return;
            }

        } else {
            saldo += valor;
        }

        registrarOperacao("Depósito", valor);
        System.out.println(
                "\n💰 Depósito realizado com sucesso!\n" +
                        "Valor depositado: " + formatarValor(valor)
        );

    }

    @Override
    public void transferir(double valor, IConta contaDestino) {

        if (!sacarSilencioso(valor)) {
            System.out.println("Transferência não realizada. Saldo + limite insuficiente.");
            return;
        }

        contaDestino.depositarSilencioso(valor);

        registrarOperacao("Transferência enviada para " + contaDestino.getTitular(), valor);

        if (contaDestino instanceof Conta destino) {
            destino.registrarOperacao("Transferência recebida de " + this.getTitular(), valor);
        }

        System.out.println(
                "\n📤 Transferência concluída!\n" +
                        "Valor enviado: " + formatarValor(valor) + "\n" +
                        "Destinatário: " + contaDestino.getTitular()
        );

    }

    public void recarregarCelular(String numero, double valor) {

        if (valor <= 0) {
            System.out.println("Valor inválido.");
            return;
        }

        if (saldo + (ChequeEspecial - ChequeEspecialUsado) < valor) {
            System.out.println("Saldo insuficiente para fazer recarga.");
            return;
        }

        sacarSilencioso(valor);

        registrarOperacao("Recarga de Celular (" + numero + ")", valor);
        System.out.println(
                "\n📱 Recarga realizada com sucesso!\n" +
                        "Número: " + numero + "\n" +
                        "Valor: " + formatarValor(valor)
        );

    }

    public void pagarBoleto(double valor, String descricao) {

        double disponivel = saldo + (ChequeEspecial - ChequeEspecialUsado);

        if (valor > disponivel) {
            System.out.println("Pagamento não realizado. Saldo + Limite insuficiente.");
            return;
        }

        sacarSilencioso(valor);
        registrarOperacao("Pagamento de Boleto: " + descricao, valor);

        System.out.println(
                "\n📄 Boleto \"" + descricao + "\" pago com sucesso!\n" +
                        "💰 Valor Pago: " + formatarValor(valor) + "\n" +
                        "✔️ Operação concluída."
        );

    }


    // ============================================================
    //                 CONSULTAS
    // ============================================================

    public void verificarSaldo() {
        System.out.println("============= Consulta Saldo ===============");
        System.out.printf("Cliente: %s\n", cliente.getNome());
        System.out.println("Saldo: " + formatarValor(saldo));
        System.out.println("Limite Disponível de Cheque Especial: " + formatarValor(ChequeEspecial - ChequeEspecialUsado));
        System.out.println("Limite Usado de Cheque Especial: " + formatarValor(ChequeEspecialUsado));
        System.out.println("============================================");
    }

    public void consultarChequeEspecial() {
        System.out.println("======== Cheque Especial ========");
        System.out.println("Limite Total: " + formatarValor(ChequeEspecial));
        System.out.println("Limite Usado: " + formatarValor(ChequeEspecialUsado));
        System.out.println("Limite Disponível: " + formatarValor(ChequeEspecial - ChequeEspecialUsado));
    }

    // ============================================================
    // GETTERS
    // ============================================================

    public int getAgencia() {
        return agencia;
    }

    public int getNumero() {
        return numero;
    }

    public double getSaldo() {
        return saldo;
    }

    public String getTitular() {
        return cliente != null ? cliente.getNome() : "";
    }

    public abstract String getTipoConta();

    public double getSaldoReal() {
        // saldo real = saldo - cheque especial usado
        return saldo - ChequeEspecialUsado;
    }



    protected void imprimirInfosComuns() {
        System.out.println("Titular: " + cliente.getNome());
        System.out.println("Agencia: " + agencia);
        System.out.println("Número: " + numero);
        System.out.println("Saldo Atual: " + formatarValor(saldo));
        System.out.println("Limite Cheque Especial Disponível: " + formatarValor(ChequeEspecial - ChequeEspecialUsado));

    }
}

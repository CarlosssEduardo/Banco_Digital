public record Operacao(String descricao, double valor) {

    @Override
    public String toString() {
        return descricao + " - R$ " + valor;
    }
}

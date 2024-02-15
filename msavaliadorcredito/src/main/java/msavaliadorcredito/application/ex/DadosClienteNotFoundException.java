package msavaliadorcredito.application.ex;

public class DadosClienteNotFoundException extends Exception {
    public DadosClienteNotFoundException(){
        super("Dados do cliente nao encontrados para o CPF informado.");
    }
}

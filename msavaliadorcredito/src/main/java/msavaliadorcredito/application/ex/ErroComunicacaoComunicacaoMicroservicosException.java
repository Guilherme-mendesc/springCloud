package msavaliadorcredito.application.ex;

import lombok.Getter;

public class ErroComunicacaoComunicacaoMicroservicosException extends Exception{

    @Getter
    private Integer status;
    public ErroComunicacaoComunicacaoMicroservicosException(String msg, Integer status){
        super(msg);
        this.status = status;

    }
}

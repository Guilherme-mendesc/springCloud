package io.github.Guilhermemendesc.mscartoes.application.dto;

import io.github.Guilhermemendesc.mscartoes.domain.ClienteCartao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartoesPorCLienteResponse {
    private String nome;
    private String bandeira;
    private BigDecimal limiteLiberado;

    public static CartoesPorCLienteResponse fromModel(ClienteCartao model){
        return new CartoesPorCLienteResponse(
                model.getCartao().getNome(),
                model.getCartao().getBandeira().toString(),
                model.getLimite()
        );
    }

}

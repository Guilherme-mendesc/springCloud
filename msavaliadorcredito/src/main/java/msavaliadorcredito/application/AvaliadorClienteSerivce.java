package msavaliadorcredito.application;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import msavaliadorcredito.application.domain.model.*;
import msavaliadorcredito.application.ex.DadosClienteNotFoundException;
import msavaliadorcredito.application.ex.ErroComunicacaoComunicacaoMicroservicosException;
import msavaliadorcredito.infra.clients.CartoesResourceClient;
import msavaliadorcredito.infra.clients.ClienteResourceClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliadorClienteSerivce {

    private final ClienteResourceClient clientesClient;
    private final CartoesResourceClient cartoesClient;

    public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClienteNotFoundException,
            ErroComunicacaoComunicacaoMicroservicosException {
        //obterDadosCliente = MSCLIENTE
        // obterCartoesCliente = MSCARTOES

        try {

            ResponseEntity<DadosCliente> dadosClienteResponse= clientesClient.dadosCliente(cpf);
            ResponseEntity<List<CartaoCliente>> cartoesResponse = cartoesClient.getCartoesByCliente(cpf);

        return SituacaoCliente
                .builder()
                .cliente(dadosClienteResponse.getBody())
                .cartoes(cartoesResponse.getBody())
                .build();

             }catch (FeignException.FeignClientException e){
            int status = e.status();
            if(HttpStatus.NOT_FOUND.value()== status){
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoComunicacaoMicroservicosException(e.getMessage(), status);
        }
    }

    public RetornoAvaliacaoCliente realizarAvaliacao(String cpf, Long renda) throws DadosClienteNotFoundException,
            ErroComunicacaoComunicacaoMicroservicosException{
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clientesClient.dadosCliente(cpf);
            ResponseEntity<List<Cartao>> cartoesResponse= cartoesClient.getCartoesRendaAteh(renda);

            List<Cartao> cartoes = cartoesResponse.getBody();
            var listaCartoesAprovados = cartoes.stream().map(cartao ->{
                DadosCliente dadosCliente = dadosClienteResponse.getBody();

                BigDecimal limiteBasico = cartao.getLimiteBasico();
                BigDecimal idadeBD = BigDecimal.valueOf(dadosCliente.getIdade());
                var fator = idadeBD.divide(BigDecimal.valueOf(10));
                BigDecimal limiteAprovado = fator.multiply(limiteBasico);

                CartaoAprovado aprovado = new CartaoAprovado();
                aprovado.setCartao(cartao.getNome());
                aprovado.setBandeira(cartao.getBandeira());
                aprovado.setLimiteAprovado(limiteAprovado);

                return aprovado;
            }).collect(Collectors.toList());

            return new RetornoAvaliacaoCliente(listaCartoesAprovados);

        }catch (FeignException.FeignClientException e){
            int status = e.status();
            if(HttpStatus.NOT_FOUND.value()== status){
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoComunicacaoMicroservicosException(e.getMessage(), status);
        }
    }

}

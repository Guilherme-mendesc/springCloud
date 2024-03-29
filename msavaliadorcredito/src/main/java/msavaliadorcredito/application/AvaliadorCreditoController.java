package msavaliadorcredito.application;


import lombok.RequiredArgsConstructor;
import msavaliadorcredito.application.domain.model.*;
import msavaliadorcredito.application.ex.DadosClienteNotFoundException;
import msavaliadorcredito.application.ex.ErroComunicacaoComunicacaoMicroservicosException;
import msavaliadorcredito.application.ex.ErroSolicitacaoCartaoException;
import msavaliadorcredito.infra.clients.CartoesResourceClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("avaliacoes-credito")
public class AvaliadorCreditoController {

    private final AvaliadorClienteSerivce avaliadorCreditoService;
    private final CartoesResourceClient cartoesClient;


    @GetMapping
    public String status(){
        return "ok";
    }


    @GetMapping(value = "situacao-cliente", params ="cpf")
    public ResponseEntity consultaSituacaoCliente(@RequestParam("cpf") String cpf){
        try {
            SituacaoCliente situacaoCliente =  situacaoCliente = avaliadorCreditoService.obterSituacaoCliente(cpf);
            return ResponseEntity.ok(situacaoCliente);
        } catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoComunicacaoMicroservicosException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity realizarAvaliacao(@RequestBody DadosAvaliacao dados){
        try {
            RetornoAvaliacaoCliente retornoAvaliacaoCliente = avaliadorCreditoService
                    .realizarAvaliacao(dados.getCpf(), dados.getRenda());
            return ResponseEntity.ok(retornoAvaliacaoCliente);
        } catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoComunicacaoMicroservicosException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }

    }

    @PostMapping("solicitacoes-cartao")
    public ResponseEntity solicitarCartao(@RequestBody DadosSolicitacaoEmissaoCartao dados){
        try {
            ProtocoloSolicitacaoCartao protocoloSolicitacaoCartao= avaliadorCreditoService
                    .solicitarEmissaoCartao(dados);
            return ResponseEntity.ok(protocoloSolicitacaoCartao);
        }catch (ErroSolicitacaoCartaoException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}

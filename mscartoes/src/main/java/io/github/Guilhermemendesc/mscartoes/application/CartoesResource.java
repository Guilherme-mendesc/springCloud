package io.github.Guilhermemendesc.mscartoes.application;


import io.github.Guilhermemendesc.mscartoes.application.dto.CartaoSaveRequest;
import io.github.Guilhermemendesc.mscartoes.application.dto.CartoesPorCLienteResponse;
import io.github.Guilhermemendesc.mscartoes.domain.Cartao;
import io.github.Guilhermemendesc.mscartoes.domain.ClienteCartao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("cartoes")
@RequiredArgsConstructor
public class CartoesResource {

    private final CartaoService cartaoService;
    private final ClienteCartaoService clienteCartaoService;

    @GetMapping
    public String status(){
        return "OK";
    }

    @PostMapping
    public ResponseEntity cadastra(@RequestBody CartaoSaveRequest request){
        Cartao cartao = request.toModel();
        cartaoService.save(cartao);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCartoesRendaAteh(@RequestParam("renda") Long renda){
        List<Cartao> list = cartaoService.getCartoesRendaMenorIgual(renda);
        return ResponseEntity.ok(list);
    }
    @GetMapping(params= "cpf")
    public ResponseEntity<List<CartoesPorCLienteResponse>> getCartoesByCliente(
            @RequestParam("cpf")String cpf){
       List<ClienteCartao> lista = clienteCartaoService.listCartoesByCpf(cpf);
       List<CartoesPorCLienteResponse> resultList = lista.stream()
               .map(CartoesPorCLienteResponse::fromModel)
               .collect(Collectors.toList());
       return ResponseEntity.ok(resultList);

    }

}

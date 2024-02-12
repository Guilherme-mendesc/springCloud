package io.github.Guilhermemendesc.msclientes.application;

import io.github.Guilhermemendesc.msclientes.infra.repository.ClienteRepository;
import io.github.Guilhermemendesc.msclientes.domain.Cliente;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    @Transactional
    public Cliente save(Cliente cliente){
    return repository.save(cliente);
    }

    public Optional<Cliente> getByCPF(String cpf){
        return repository.findByCpf(cpf);
    }


}

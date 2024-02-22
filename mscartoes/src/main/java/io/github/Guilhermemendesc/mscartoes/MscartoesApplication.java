package io.github.Guilhermemendesc.mscartoes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableRabbit
@Slf4j
public class MscartoesApplication {


	public static void main(String[] args) {
		log.info("INFORMACOES : {}", "TESTE INFO" );
		log.error("ERRO : {}", "TESTE ERROR" );
		log.warn("AVISO : {}", "TESTE WARN" );
		SpringApplication.run(MscartoesApplication.class, args);
	}

}

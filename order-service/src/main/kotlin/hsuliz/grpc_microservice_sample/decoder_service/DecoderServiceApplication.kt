package hsuliz.grpc_microservice_sample.decoder_service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DecoderServiceApplication

fun main(args: Array<String>) {
	runApplication<DecoderServiceApplication>(*args)
}

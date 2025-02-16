package hsuliz.grpc_microservice_sample.order_service

import hsuliz.grpc_microservice_sample.order_service.proto.CryptographServiceGrpc
import hsuliz.grpc_microservice_sample.order_service.proto.TextToEncode
import io.grpc.ManagedChannelBuilder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class Handler {
  private val channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build()
  private val stub = CryptographServiceGrpc.newBlockingStub(channel)

  fun encodeMessage(request: ServerRequest): Mono<ServerResponse> {
    val textToEncode = TextToEncode.newBuilder().setText("Hello World").build()
    val response = stub.encodeText(textToEncode)

    return ServerResponse.ok().bodyValue(response)
  }
}

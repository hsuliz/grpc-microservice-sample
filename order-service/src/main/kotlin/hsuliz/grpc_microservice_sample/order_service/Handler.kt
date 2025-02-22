package hsuliz.grpc_microservice_sample.order_service

import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.MoreExecutors
import hsuliz.grpc_microservice_sample.order_service.proto.CryptographServiceGrpc
import hsuliz.grpc_microservice_sample.order_service.proto.DecodeTextRequest
import hsuliz.grpc_microservice_sample.order_service.proto.DecodeTextResponse
import io.grpc.ManagedChannelBuilder
import org.springframework.beans.factory.annotation.Value

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class Handler(
    @Value("\${grpc.address}")
    val grpcAddress: String,
    @Value("\${grpc.port}")
    val grpcPort: Int,
) {
  private val channel = ManagedChannelBuilder.forAddress(grpcAddress, grpcPort).usePlaintext().build()
  private val stub = CryptographServiceGrpc.newFutureStub(channel)

  fun decodeMessage(request: ServerRequest): Mono<ServerResponse> {
    data class Request(val text: String, val runes: Int)

    return request
        .bodyToMono(Request::class.java)
        .flatMap { requestBody ->
          val decodeRequest =
              DecodeTextRequest.newBuilder()
                  .setText(requestBody.text)
                  .setRunes(requestBody.runes)
                  .build()

          Mono.create<String> { sink ->
            val responseFuture = stub.decodeText(decodeRequest)
            Futures.addCallback(
                responseFuture,
                object : FutureCallback<DecodeTextResponse> {
                  override fun onSuccess(result: DecodeTextResponse?) {
                    sink.success(result?.text ?: "Error: Empty response")
                  }

                  override fun onFailure(t: Throwable) {
                    sink.error(t)
                  }
                },
                MoreExecutors.directExecutor())
          }
        }
        .flatMap { text -> ServerResponse.ok().bodyValue(text) }
  }
}

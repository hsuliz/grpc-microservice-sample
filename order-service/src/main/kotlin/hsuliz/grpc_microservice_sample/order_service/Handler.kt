package hsuliz.grpc_microservice_sample.order_service

import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.MoreExecutors
import hsuliz.grpc_microservice_sample.order_service.proto.CryptographServiceGrpc
import hsuliz.grpc_microservice_sample.order_service.proto.EncodeTextRequest
import hsuliz.grpc_microservice_sample.order_service.proto.EncodeTextResponse
import io.grpc.ManagedChannelBuilder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class Handler {
  private val channel = ManagedChannelBuilder.forAddress("localhost", 8090).usePlaintext().build()
  private val stub = CryptographServiceGrpc.newFutureStub(channel)

  fun encodeMessage(request: ServerRequest): Mono<ServerResponse> {
    val textToEncode = EncodeTextRequest.newBuilder().setText("Hello World").build()

    return Mono.create { sink ->
          val responseFuture = stub.encodeText(textToEncode)
          Futures.addCallback(
              responseFuture,
              object : FutureCallback<EncodeTextResponse> {
                override fun onSuccess(result: EncodeTextResponse?) {
                  sink.success(result?.text)
                }

                override fun onFailure(t: Throwable) {
                  sink.error(t)
                }
              },
              MoreExecutors.directExecutor())
        }
        .flatMap { text -> ServerResponse.ok().bodyValue(text) }
  }
}

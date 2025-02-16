package hsuliz.grpc_microservice_sample.order_service

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class RouteConfig() {
  @Bean fun helloWorldRouter(handler: Handler) = router { POST("/encode", handler::encodeMessage) }
}

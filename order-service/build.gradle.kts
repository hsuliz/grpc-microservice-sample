import com.google.protobuf.gradle.id
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
  kotlin("jvm") version "1.9.25"
  kotlin("plugin.spring") version "1.9.25"
  id("org.springframework.boot") version "3.4.2"
  id("io.spring.dependency-management") version "1.1.7"
  id("com.google.protobuf") version "0.9.4"
  id("com.ncorti.ktfmt.gradle") version "0.22.0"
}

group = "hsuliz.grpc-microservice-sample"

version = "latest"

tasks.named<BootBuildImage>("bootBuildImage") {
  imageName.set("hsuliz/gms-order-service")
}

java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }

repositories { mavenCentral() }

extra["springGrpcVersion"] = "0.3.0"

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("io.grpc:grpc-services")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
  implementation("org.springframework.grpc:spring-grpc-spring-boot-starter")
  developmentOnly("org.springframework.boot:spring-boot-devtools")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("io.projectreactor:reactor-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
  testImplementation("org.springframework.grpc:spring-grpc-test")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
  imports {
    mavenBom("org.springframework.grpc:spring-grpc-dependencies:${property("springGrpcVersion")}")
  }
}

kotlin { compilerOptions { freeCompilerArgs.addAll("-Xjsr305=strict") } }

protobuf {
  protoc { artifact = "com.google.protobuf:protoc" }
  plugins { id("grpc") { artifact = "io.grpc:protoc-gen-grpc-java" } }
  generateProtoTasks {
    all().forEach {
      it.plugins {
        id("grpc") {
          option("jakarta_omit")
          option("@generated=omit")
        }
      }
    }
  }
}

tasks.withType<Test> { useJUnitPlatform() }

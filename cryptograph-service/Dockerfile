FROM golang:1.23 AS builder
WORKDIR /build

# Install dependecies
COPY go.mod go.sum ./
RUN go mod download

# Build app
COPY . .
RUN CGO_ENABLED=0 GOOS=linux go build -o /cryptograph-service ./cmd/grpc/main.go

FROM debian:11-slim
WORKDIR /app

COPY --from=builder /cryptograph-service /cryptograph-service

EXPOSE 8080

CMD ["/cryptograph-service"]
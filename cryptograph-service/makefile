APP_NAME := cryptograph-service

.PHONY: build

# App
clean:
	rm "$(APP_NAME)"

build:
	go build -o "$(APP_NAME)" ./cmd/grpc

run:
	./$(APP_NAME)

# Docker
build-image:
	docker build -t hsuliz/gms-cryptograph-service .

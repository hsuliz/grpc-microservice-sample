APP_NAME := cryptograph-service

.PHONY: build

clean:
	rm "$(APP_NAME)"

build:
	go build -o "$(APP_NAME)" ./cmd/grpc

run:
	./$(APP_NAME)
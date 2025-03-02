package main

import (
	"cryptograph-service/cmd/grpc/server"
	"cryptograph-service/internal/pb"
	"fmt"
	"google.golang.org/grpc"
	"log"
	"net"
)

func main() {
	lis, err := net.Listen("tcp", fmt.Sprintf(":%d", 8080))
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}

	log.Printf("server listening at %v", lis.Addr())
	grpcServer := grpc.NewServer()
	pb.RegisterCryptographServiceServer(grpcServer, &server.Server{})
	if err := grpcServer.Serve(lis); err != nil {
		log.Fatalf("failed to serve: %v", err)
	}
}

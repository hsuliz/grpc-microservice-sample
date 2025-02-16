package main

import (
	"context"
	"flag"
	"fmt"
	"google.golang.org/grpc"
	"log"
	"net"
	pb "cryptograph-service/proto"
)

var (
	port = flag.Int("port", 8090, "The server port")
)

type server struct {
	pb.CryptographServiceServer
}


func (s *server) EncodeText(_ context.Context, in *pb.EncodeTextRequest) (*pb.EncodeTextResponse, error) {
	log.Printf("Received")
	log.Println(in)
	return &pb.EncodeTextResponse{Text: "Hello"}, nil
}

func main() {
	flag.Parse()
	lis, err := net.Listen("tcp", fmt.Sprintf(":%d", *port))
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}
	s := grpc.NewServer()
	pb.RegisterCryptographServiceServer(s, &server{})
	log.Printf("server listening at %v", lis.Addr())
	if err := s.Serve(lis); err != nil {
		log.Fatalf("failed to serve: %v", err)
	}
}

package main

import (
	"context"
	"cryptograph-service/cracker"
	pb "cryptograph-service/proto"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
	"log"
)

type server struct {
	pb.CryptographServiceServer
}

func (s *server) DecodeText(_ context.Context, req *pb.DecodeTextRequest) (*pb.DecodeTextResponse, error) {
	log.Println("got request:", req)
	text, err := cracker.CrackHash(req.Text, req.Runes)
	if err != nil {
		log.Println("error cracking hash:", err)
		return nil, status.Errorf(codes.NotFound, "failed to decode hash: %v", err)
	}
	return &pb.DecodeTextResponse{Text: text}, nil
}

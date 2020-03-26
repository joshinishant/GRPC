package com.communication.grpc.service;

import com.communication.grpc.service.ChatBot.Request;
import com.communication.grpc.service.ChatBot.Response;
import com.communication.grpc.service.ChatGrpc.ChatImplBase;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatService extends ChatImplBase {

    Logger logger= LoggerFactory.getLogger(ChatService.class);


    @Override
    public void sendMessage(Request request, StreamObserver<Response> responseObserver) {
        String responseMessage=request.getRequest().contains("?")?"I will get back to you":"sounds great";
        Response response= Response.newBuilder().setResponse(responseMessage).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Request> bidirectionalStreaming(StreamObserver<Response> responseObserver) {
        return new StreamObserver<Request>() {
            @Override
            public void onNext(Request request) {
                logger.info("Request: "+request.getRequest());
                if(request.getRequest().contains("Hi")){
                    responseObserver.onNext(Response.newBuilder().setResponse("Hello!!").build());
                }else if(request.getRequest().contains("?")){
                    responseObserver.onNext(Response.newBuilder().setResponse("I will get back to you").build());
                }else {
                    responseObserver.onNext(Response.newBuilder().setResponse("Hmm...").build());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                logger.error("Error occurred during conversation",throwable);
            }

            @Override
            public void onCompleted() {
                logger.info("Conversation completed");
            }
        };

    }
}

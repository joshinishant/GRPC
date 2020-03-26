package com.communication.grpc.server;

import com.communication.grpc.service.ChatService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ServerImpl {

    private static final Logger logger= LoggerFactory.getLogger(ServerImpl.class);



    public static void main(String args[]) throws IOException,InterruptedException {
        Server server=createServer();
        server.start();
        logger.info("Server started");
        server.awaitTermination();

    }

    private static Server createServer() {
        try{
            return ServerBuilder.forPort(50051).addService(new ChatService()).build();
        }catch (Exception e){
            logger.error("Exception occurred while starting server. ",e);
        }
        return null;
    }

}

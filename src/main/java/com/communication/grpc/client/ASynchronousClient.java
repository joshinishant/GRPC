package com.communication.grpc.client;

import com.communication.grpc.service.ChatBot.Request;
import com.communication.grpc.service.ChatBot.Response;
import com.communication.grpc.service.ChatGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ASynchronousClient {

	public static final Logger logger= LoggerFactory.getLogger(ASynchronousClient.class);


	public static void main(String[] args) {
		String target;
		List<Request> requestList= new ArrayList<>();

		if(args.length == 0){
			logger.error("Please Pass target -> (serverAddress:port) and Message/s");
			return;
		}else if(args.length == 1){
			logger.error("Please Pass Message/s");
			return;
		}else {
			target=args[0];
			for(int i=1;i<args.length;i++){
				requestList.add(Request.newBuilder().setRequest(args[i]).build());
			}
		}

		ManagedChannel channel= ManagedChannelBuilder.forTarget(target).usePlaintext().build();
		ChatGrpc.ChatStub nonblockingStub=ChatGrpc.newStub(channel);
			StreamObserver<Request> requestStream=nonblockingStub.bidirectionalStreaming(new StreamObserver<Response>() {
				@Override
				public void onNext(Response response) {
					logger.info("Response: "+response.getResponse());
				}

				@Override
				public void onError(Throwable throwable) {
					logger.error("Error occurred during conversation",throwable);
				}

				@Override
				public void onCompleted() {
					logger.info("Conversation completed");
				}
			});

		logger.info("Sending Data");
		requestStream.onNext(Request.newBuilder().setRequest("Hi").build());
		requestStream.onNext(Request.newBuilder().setRequest("How are you?").build());
		requestStream.onCompleted();
	}
}

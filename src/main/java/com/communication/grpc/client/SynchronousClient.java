package com.communication.grpc.client;

import com.communication.grpc.service.ChatBot.Request;
import com.communication.grpc.service.ChatBot.Response;
import com.communication.grpc.service.ChatGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SynchronousClient{

	public static final Logger logger= LoggerFactory.getLogger(SynchronousClient.class);


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
		ChatGrpc.ChatBlockingStub stub=ChatGrpc.newBlockingStub(channel);
		for (Request request:requestList){
			Response response=stub.sendMessage(request);
			logger.info("Response: "+response.getResponse());
		}



	}
}

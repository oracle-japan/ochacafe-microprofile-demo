/*
 * Copyright (c) 2024 Oracle and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package oracle.demo.grpc;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import io.helidon.grpc.api.Grpc;
import io.helidon.microprofile.grpc.client.GrpcConfigurablePort;
import io.helidon.microprofile.testing.junit5.HelidonTest;

import io.grpc.stub.StreamObserver;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.WebTarget;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@HelidonTest
class HelloWorldServiceTest {

    @Inject
    private WebTarget webTarget;

    @Inject
    @Grpc.GrpcProxy
    private HelloWorldServiceClient client;

    @BeforeEach
    void updatePort() {
        if (client instanceof GrpcConfigurablePort c) {
            c.channelPort(webTarget.getUri().getPort());
        }
    }

    @Test
    void sayHello() {
        HelloReply res = client.sayHello(HelloRequest.newBuilder().setName("Felix").build());
        assertThat(res.getMessage(), is("Hello Felix"));
    }

    @Test
    void sayHelloStream() {
        Stream<HelloReply> stream = client.sayHelloStreamReply(HelloRequest.newBuilder().setName("Simon").build());
        List<String> list = stream.map(HelloReply::getMessage).toList();
        String[] value = list.toArray(new String[list.size()]);
        assertArrayEquals(new String[]{"Hello", "Simon"}, value);
    }

    @Test
    void sayHelloBidiStream() {

        HelloReplyStreamObserver observer = new HelloReplyStreamObserver();
        
        StreamObserver<HelloRequest> request = client.sayHelloBidiStream(observer);

        request.onNext(HelloRequest.newBuilder().setName("Bob").build());
        request.onNext(HelloRequest.newBuilder().setName("Simon").build());
        request.onNext(HelloRequest.newBuilder().setName("Felix").build());
        request.onCompleted();

        assertArrayEquals(new String[]{"Bob", "Simon", "Felix"}, observer.getMessages());
    }

    public class HelloReplyStreamObserver implements StreamObserver<HelloReply>{

        private final Logger logger = Logger.getLogger(HelloReplyStreamObserver.class.getName());

        private ArrayList<String> messages = new ArrayList<String>();

        private boolean completed = false;

        public String[] getMessages(){
            waitForCompletion();
            return messages.toArray(new String[messages.size()]);
        }

        @Override
        public void onNext(HelloReply value) {
            logger.info("onNext(): " + value.getMessage());
            messages.add(value.getMessage());
        }

        @Override
        public void onError(Throwable t) {
            logger.warning("onError(): " + t.getMessage());
        }

        @Override
        public void onCompleted() {
            logger.info("onCompleted()");
            notifyCompletion();
        }
     
        public synchronized void waitForCompletion(){
            if(!completed){
                logger.info("waiting for onCompleted()");
                try{
                    this.wait();
                }catch(InterruptedException e){}
                logger.info("received onCompleted()");
            }
        }

        public synchronized void notifyCompletion(){
            completed = true;
            this.notifyAll();            
        }
    }


}


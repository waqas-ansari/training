package org.sterl;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;

import org.junit.Test;
import org.sterl.proto.TestMessage;
import org.sterl.proto.TestMessage.JumpData;
import org.sterl.proto.TestMessage.MoveData;
import org.sterl.proto.TestMessage.RequestMessage;
import org.sterl.proto.TestMessage.RequestMessage.Builder;
import org.sterl.proto.TestMessage.RequestMessage.MessageType;

public class SerializationProtoTest extends AbstractTest{

    @Test
    public void testProto() throws Exception {
        long buildMessage = 0;
        long serialization = 0;
        long deserialization = 0;
       
        long time = 0;
        long messageSize = 0;
        
        RequestMessage read;
        Builder newBuilder = TestMessage.RequestMessage.newBuilder();
        org.sterl.proto.TestMessage.JumpData.Builder jumpBulder = JumpData.newBuilder();
        org.sterl.proto.TestMessage.MoveData.Builder moveBuilder = MoveData.newBuilder();
        for (int i = 0; i < CYCLES; i++) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            time = System.nanoTime();
            RequestMessage message = newBuilder
                    .setType(MessageType.JUMP)
                    .setJumpData(jumpBulder.setHowFar(10).setHowHigh(650))
                    .setMoveData(moveBuilder.setSpeed(500))
                    .build();
            newBuilder.clear();
            buildMessage += System.nanoTime() - time;
            
            time = System.nanoTime();
            message.writeTo(out);
            serialization += System.nanoTime() - time;
            
            byte[] msg = out.toByteArray();
            time = System.nanoTime();
            read = RequestMessage.parseFrom(msg) ;
            deserialization += System.nanoTime() - time;
            
            messageSize = msg.length;
            assertEquals(message, read);
        }
        
        System.out.println("*** Proto Stats ***");
        writeStats(buildMessage, serialization, deserialization, messageSize);
    }
}
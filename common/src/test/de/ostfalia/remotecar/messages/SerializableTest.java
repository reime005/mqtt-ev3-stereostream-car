package de.ostfalia.remotecar.messages;

import de.ostfalia.remotecar.message.MotionControlMessage;
import de.ostfalia.remotecar.message.ParseException;
import de.ostfalia.remotecar.message.VideoControlMessage;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * Created by Marius Reimers Reimer on 17/03/17.
 */
public class SerializableTest {

    @Test
    public void serializableTest() throws ParseException {
        final VideoControlMessage videoControlMessage = new VideoControlMessage("10.0.0.1", 2560, 720, 8080, 30);

        try {
            FileOutputStream fileOut = new FileOutputStream("/tmp/VideoControlMessage.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(videoControlMessage);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in /tmp/VideoControlMessage.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}

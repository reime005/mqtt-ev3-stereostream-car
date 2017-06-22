package de.ostfalia.remotecar.messages;

import de.ostfalia.remotecar.message.MotionControlMessage;
import de.ostfalia.remotecar.message.ParseException;
import de.ostfalia.remotecar.message.VideoControlMessage;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * Created by Marius Reimers Reimer on 17/03/17.
 */
public class MessagesTest {

    @Test
    public void motionControlMessageTest() throws ParseException {
        final MotionControlMessage motionControlMessage = new MotionControlMessage(MotionControlMessage.SpeedParameter.FORWARD, MotionControlMessage.SteeringParameter.LEFT);
        final MqttMessage mqttMsg = new MqttMessage(motionControlMessage.toString().getBytes());
        final MotionControlMessage parsedMotionControlMessage = MotionControlMessage.asString(new String(mqttMsg.getPayload(), StandardCharsets.UTF_8));
        assertEquals(motionControlMessage.getSpeedParameter(), parsedMotionControlMessage.getSpeedParameter());
        assertEquals(motionControlMessage.getSteeringParameter(), parsedMotionControlMessage.getSteeringParameter());
    }

    @Test
    public void videoControlMessageTest() throws ParseException {
        final VideoControlMessage videoControlMessage = new VideoControlMessage("10.0.0.1", 2560, 720, 8080, 30);
        System.out.println(videoControlMessage.toString());
        final MqttMessage mqttMsg = new MqttMessage(videoControlMessage.toString().getBytes());
        final VideoControlMessage parsedVideoControlMessage = VideoControlMessage.asString(new String(mqttMsg.getPayload(), StandardCharsets.UTF_8));
        assertEquals(videoControlMessage.getUdpSinkIp(), parsedVideoControlMessage.getUdpSinkIp());
        assertEquals(videoControlMessage.getUdpSinkPort(), parsedVideoControlMessage.getUdpSinkPort());
        assertEquals(videoControlMessage.getVideoFps(), parsedVideoControlMessage.getVideoFps());
        assertEquals(videoControlMessage.getVideoHeight(), parsedVideoControlMessage.getVideoHeight());
        assertEquals(videoControlMessage.getVideoWidth(), parsedVideoControlMessage.getVideoWidth());
    }

    @Test
    public void motionControlMessageFailTest() throws ParseException {
        final MotionControlMessage motionControlMessage = new MotionControlMessage(MotionControlMessage.SpeedParameter.FORWARD, MotionControlMessage.SteeringParameter.LEFT);
        final MqttMessage mqttMsg = new MqttMessage(motionControlMessage.toString().getBytes());
        final MotionControlMessage parsedMotionControlMessage = MotionControlMessage.asString(new String(mqttMsg.getPayload(), StandardCharsets.UTF_8));
        assertNotEquals(parsedMotionControlMessage.getSpeedParameter(), MotionControlMessage.SpeedParameter.BACKWARD);
        assertNotEquals(parsedMotionControlMessage.getSteeringParameter(), MotionControlMessage.SteeringParameter.RIGHT);
    }

    @Test
    public void videoControlMessageFailTest() throws ParseException {
        final VideoControlMessage videoControlMessage = new VideoControlMessage("10.0.0.1", 2560, 720, 8080, 30);
        final MqttMessage mqttMsg = new MqttMessage(videoControlMessage.toString().getBytes());
        final VideoControlMessage parsedVideoControlMessage = VideoControlMessage.asString(new String(mqttMsg.getPayload(), StandardCharsets.UTF_8));
        assertNotEquals(parsedVideoControlMessage.getUdpSinkIp(), "10.0.0.2");
        assertNotEquals(parsedVideoControlMessage.getUdpSinkPort(), "8081");
        assertNotEquals(parsedVideoControlMessage.getVideoFps(), 15);
        assertNotEquals(parsedVideoControlMessage.getVideoHeight(), 1344);
        assertNotEquals(parsedVideoControlMessage.getVideoWidth(), 376);
    }
}

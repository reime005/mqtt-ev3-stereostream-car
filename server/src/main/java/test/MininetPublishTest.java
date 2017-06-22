package test;

import de.ostfalia.remotecar.message.MotionControlMessage;
import de.ostfalia.remotecar.message.VideoControlMessage;
import de.ostfalia.remotecar.mqtt.MqttPublishClient;

/**
 * Created by Marius Reimer on 15.01.2017.
 */
public class MininetPublishTest {

    private static Thread mqttThread;

    private static MqttPublishClient publishClient;

    public static void main(String[] args) throws Exception {

        try {
            String[] ipPort = args[0].split(":");
            publishClient = new MqttPublishClient(ipPort[0], Integer.parseInt(ipPort[1]), Integer.parseInt(args[1]));
        } catch (Exception e) {
            System.out.println("To start with a custom IP, add the option: ");
            System.out.println("IP:PORT");
            System.exit(-1);
        }

        //  start CarServer
        publishClient.connect();
        System.out.println("Publish Client connected");

        if (publishClient.isConnected()) {
            publishClient.sendVideoControlMsg(new VideoControlMessage("10.0.0.6", 2560, 720, 1337, 30));
            System.out.println("Video control msg sent");
        }

        // send mqtt messages every second
        mqttThread = new Thread(() -> {
            while (true) {
                try {
                    if (publishClient.isConnected()) {
                        publishClient.sendMotionControlMsg(new MotionControlMessage(MotionControlMessage.SpeedParameter.FORWARD, MotionControlMessage.SteeringParameter.STRAIGHT));
                        System.out.println("messages sent at " + System.currentTimeMillis());
                    }
                    Thread.sleep(5000);
                } catch (Exception e) {
                    System.out.println("Exception in Thread: " + mqttThread.getName());
                    publishClient.disconnect();
                    System.exit(-1);
                }
            }
        });

        mqttThread.start();

        // clean up when program is killed
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (publishClient != null) {
                publishClient.disconnect();
            }
        }));
    }
}

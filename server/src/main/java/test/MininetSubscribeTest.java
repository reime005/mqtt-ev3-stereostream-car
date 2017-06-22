package test;

import de.ostfalia.remotecar.mqtt.AbstractMqttSubscribeClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by Marius Reimer on 15.01.2017.
 */
public class MininetSubscribeTest {

    private static AbstractMqttSubscribeClient subscribeClient;

    public static void main(String[] args) {

        try {
            String[] ipPort = args[0].split(":");
            subscribeClient = new CustomSubscribeClientAbstract(ipPort[0], Integer.parseInt(ipPort[1]), Integer.parseInt(args[1]));
        } catch (Exception e) {
            System.out.println("To start with a custom IP, add the option: ");
            System.out.println("IP:PORT QOS");
            System.exit(-1);
        }

        //  start CarServer
        subscribeClient.connect();
        System.out.println("Subscribe Client connected");
        subscribeClient.subscribe();
        System.out.println("subscribed");

        // clean up when program is killed
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (subscribeClient != null) {
                subscribeClient.disconnect();
            }
        }));

//        while (true) {
//            try {
//
//            } catch (Exception e) {
//                subscribeClient.disconnect();
//                System.exit(-1);
//            }
//        }
    }

    private static class CustomSubscribeClientAbstract extends AbstractMqttSubscribeClient {

        CustomSubscribeClientAbstract(String ip, int port, int qos) {
            super(ip, port, qos);
        }

        @Override
        public void messageArrived(String s, MqttMessage message) throws Exception {
            System.out.println("messageArrived at " + System.currentTimeMillis());
            super.messageArrived(s, message);
        }
    }
}

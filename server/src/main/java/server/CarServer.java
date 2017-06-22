package server;

import de.ostfalia.remotecar.Constants;

import java.util.logging.Logger;

/**
 * Created by Marius Reimers on 29.11.2016.
 */
public class CarServer {

    private static MqttCarSubscribeClient client;
    private static Logger logger = Logger.getLogger(CarServer.class.getCanonicalName());

    public static void main(String[] args) throws Exception {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (client != null) {
                    client.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        try {
            String[] ipPort = args[0].split(":");
            client = new MqttCarSubscribeClient(ipPort[0], Integer.parseInt(ipPort[1]), Integer.parseInt(args[1]));
        } catch (Exception e) {
            client = new MqttCarSubscribeClient();
            logger.info("To start with a custom IP, add the option: ");
            logger.info("IP:PORT QOS");
        }

        //  start CarServer
        client.connect();
        client.subscribe();

//        new Thread(() -> client.subscribe()).start();

        // start EV3
        new Thread(() -> {
            try {
                while (true) {
                    try {
                        client.init();
                        return;
                    } catch (Exception e) {
                        Thread.sleep(Constants.MQTT_RECONNECT_MILLIS);
                        logger.warning(e.getLocalizedMessage());
                    }
                }
            } catch (Exception e) {
                logger.warning(e.getLocalizedMessage());
            }
        }).start();
    }
}

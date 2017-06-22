package de.ostfalia.remotecar.mqtt;

import de.ostfalia.remotecar.Constants;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by Marius Reimer on 29.11.2016.
 */
public abstract class AbstractMqttSubscribeClient extends AbstractMqttClient {

    public AbstractMqttSubscribeClient() {
        super(null, Constants.DEFAULT_SUBSCRIBE_CLIENT_ID,
                Constants.DEFAULT_RECEIVER_USER_NAME, Constants.DEFAULT_RECEIVER_USER_PASSWORD);
        this.qos = Integer.parseInt(Constants.DEFAULT_SUBSCRIBE_QOS);
    }

    public AbstractMqttSubscribeClient(String ip, int port, int qos) {
        super(new BrokerConnectInfo(ip, port), Constants.DEFAULT_SUBSCRIBE_CLIENT_ID,
                Constants.DEFAULT_RECEIVER_USER_NAME, Constants.DEFAULT_RECEIVER_USER_PASSWORD);
        this.qos = qos;
    }

    @Override
    public void connectionLost(Throwable throwable) {
        super.connectionLost(throwable);
        subscribe();
    }

    public void subscribe() {
        while (true) {
            if (!isConnected()) {
                logger.info(Constants.MQTT_MESSAGE_NOT_CONNECTED);
            } else {

                try {
                    final IMqttToken token = client.subscribe(new String[]{Constants.MQTT_TOPIC_STEERING_MESSAGE, Constants.MQTT_TOPIC_STOP_MESSAGE, Constants.MQTT_TOPIC_VIDEO_CONTROL_MESSAGE},
                            new int[]{Constants.STEERING_QOS, Constants.STOP_QOS, Constants.VIDEO_QOS});
                    token.waitForCompletion();
                    logger.info("Subscribed to TOPIC: " + Constants.MQTT_TOPIC_STEERING_MESSAGE);
                    logger.info("Subscribed to TOPIC: " + Constants.MQTT_TOPIC_VIDEO_CONTROL_MESSAGE);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(Constants.MQTT_RECONNECT_MILLIS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void messageArrived(String s, MqttMessage message) throws Exception {
        logger.info("-------------------------------------------------");
        logger.info("| Topic:" + s);
        logger.info("| Message: " + message);
        logger.info("-------------------------------------------------");
    }
}

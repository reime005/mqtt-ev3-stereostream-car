package de.ostfalia.remotecar.mqtt;

import de.ostfalia.remotecar.Constants;
import de.ostfalia.remotecar.message.MotionControlMessage;
import de.ostfalia.remotecar.message.VideoControlMessage;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by Marius Reimer on 29.11.2016.
 */
public class MqttPublishClient extends AbstractMqttClient implements IMqttPublishClient {

    public MqttPublishClient(String ip, int port, int qos) {
        super(new BrokerConnectInfo(ip, port), Constants.DEFAULT_PUBLISH_CLIENT_ID,
                Constants.DEFAULT_SENDER_USER_NAME, Constants.DEFAULT_SENDER_USER_PASSWORD);
        this.qos = qos;
    }

    public MqttPublishClient() {
        super(null, Constants.DEFAULT_PUBLISH_CLIENT_ID,
                Constants.DEFAULT_SENDER_USER_NAME, Constants.DEFAULT_SENDER_USER_PASSWORD);
        this.qos = Integer.parseInt(Constants.DEFAULT_PUBLISH_QOS);
    }

    @Override
    public void sendMotionControlMsg(MotionControlMessage msg) {
        if (!isConnected()) {
            logger.warning(Constants.MQTT_MESSAGE_NOT_CONNECTED);
            return;
        }

        final MqttMessage message = new MqttMessage(msg.toString().getBytes());
        message.setQos(qos);

        try {
            logger.info(String.format(Constants.MQTT_MESSAGE_PUBLISHING_WITH_QOS, msg.toString(), Constants.MQTT_TOPIC_STEERING_MESSAGE, qos));
            client.publish(Constants.MQTT_TOPIC_STEERING_MESSAGE, message);
            logger.info(Constants.MQTT_MESSAGE_PUBLISHED);
        } catch (MqttException e) {
            logger.warning(e.getMessage());
        }
    }

    @Override
    public void sendVideoControlMsg(VideoControlMessage videoControlMessage) {
        if (!isConnected()) {
            logger.warning(Constants.MQTT_MESSAGE_NOT_CONNECTED);
            return;
        }

        final MqttMessage msg = new MqttMessage(videoControlMessage.toString().getBytes());
        msg.setQos(qos);

        try {
            logger.info(String.format(Constants.MQTT_MESSAGE_PUBLISHING_WITH_QOS, msg.toString(), Constants.MQTT_TOPIC_VIDEO_CONTROL_MESSAGE, qos));
            client.publish(Constants.MQTT_TOPIC_VIDEO_CONTROL_MESSAGE, msg);
            logger.info(Constants.MQTT_MESSAGE_PUBLISHED);
        } catch (MqttException e) {
            logger.warning(e.getMessage());
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}

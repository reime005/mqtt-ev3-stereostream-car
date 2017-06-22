package de.ostfalia.remotecar.mqtt;

import de.ostfalia.remotecar.Constants;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.logging.Logger;

/**
 * Abstract MQTT Client to handle connect and disconnect to the broker.
 * Created by Marius Reimer on 02.01.2017.
 */
public abstract class AbstractMqttClient implements ICarControlClient, MqttCallback {

    private static final MemoryPersistence persistence = new MemoryPersistence();

    protected Logger logger;
    protected MqttAsyncClient client;
    protected int qos;

    private final MqttConnectOptions connOpts;
    private final String clientId;
    private final String broker;

    public AbstractMqttClient(final BrokerConnectInfo brokerConnectInfo, String clientId, String user, String password) {
        this.clientId = clientId;
        logger = Logger.getLogger(getClass().getCanonicalName());

        BrokerConnectInfo connectInformation;

        // Use default broker connect information, if none provided
        if (brokerConnectInfo != null) {
            connectInformation = brokerConnectInfo;
        } else {
            connectInformation = new BrokerConnectInfo(Constants.DEFAULT_BROKER_IP, Integer.valueOf(Constants.DEFAULT_BROKER_PORT));
        }

        broker = Constants.BROKER_URL_PROTOCOL + connectInformation.getIp() + ":" + connectInformation.getPort();

        connOpts = new MqttConnectOptions();
        connOpts.setUserName(user);
        connOpts.setConnectionTimeout(Constants.CLIENT_CONNECT_TIMEOUT_SECONDS);
        connOpts.setPassword(password.toCharArray());
        connOpts.setCleanSession(true);
        connOpts.setKeepAliveInterval(Constants.CLIENT_KEEP_ALIVE_SECONDS);
    }

    @Override
    public void connect() {
        if (isConnected()) {
            logger.warning(Constants.MQTT_MESSAGE_ALREADY_CONNECTED);
            return;
        }

        // try to connect until succeeded
        try {
            while (true) {
                try {
                    client = new MqttAsyncClient(broker, clientId, persistence);
                    logger.info(String.format(Constants.MQTT_MESSAGE_CONNECTING, broker));
                    client.connect(connOpts);
                    client.setCallback(this);
                    logger.info(Constants.MQTT_MESSAGE_CONNECTION_ESTABLISHED);
                    return;
                } catch (MqttException e) {
                    logger.warning(e.getMessage());
                }

                logger.info(Constants.MQTT_MESSAGE_ATTEMPTING_CONNECT);
                Thread.sleep(Constants.MQTT_RECONNECT_MILLIS);
            }
        } catch (Exception e) {
            logger.warning(e.getLocalizedMessage());
        }
    }

    @Override
    public void disconnect() {
        if (isConnected()) {
            try {
                client.disconnect();
                logger.info(Constants.MQTT_MESSAGE_DISCONNECTED);
            } catch (MqttException e) {
                logger.warning(e.getMessage());
            }
        }
    }

    @Override
    public boolean isConnected() {
        return client != null && client.isConnected();
    }

    @Override
    public void connectionLost(Throwable throwable) {
        logger.info("Reason: " + throwable.getMessage());
        throwable.printStackTrace();
        logger.info(Constants.MQTT_MESSAGE_ATTEMPTING_CONNECT);

        try {
            connect();
        } catch (Exception e) {
            logger.warning(e.getMessage());
            System.exit(-1);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        // not used
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        // may be overridden
    }
}

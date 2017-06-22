package de.ostfalia.remotecar;

/**
 * Created by Marius Reimers Reimer on 05/03/17.
 */
public class Constants {

    public static final String MQTT_TOPIC_STEERING_MESSAGE = "car/control/";
    public static final String MQTT_TOPIC_VIDEO_CONTROL_MESSAGE = "car/video/";
    public static final String MQTT_TOPIC_STOP_MESSAGE = "car/control/stop/";

    public static final String GST_LAUNCH_SCRIPT = "/home/ubuntu/start.sh";
    public static final String DEFAULT_BROKER_IP = "192.168.10.4";
    public static final String DEFAULT_BROKER_PORT = "1883";
    public static final String BROKER_URL_PROTOCOL = "tcp://";
    public static final String DEFAULT_CONTROLLER_IP = "192.168.10.3";
    public static final String DEFAULT_VIDEO_UDP_PORT = "1337";
    public static final String DEFAULT_VIDEO_WIDTH = "1344";
    public static final String DEFAULT_VIDEO_HEIGHT = "376";
    public static final String DEFAULT_VIDEO_FPS = "30";
    public static final String DEFAULT_PUBLISH_QOS = "0";
    public static final String DEFAULT_SUBSCRIBE_QOS = "0";
    public static final String DEFAULT_PUBLISH_CLIENT_ID = "HTC-VIVE";
    public static final String DEFAULT_SUBSCRIBE_CLIENT_ID = "CPS";

    public static final String DEFAULT_SENDER_USER_NAME = "sender";
    public static final String DEFAULT_SENDER_USER_PASSWORD = "ostfalia";
    public static final String DEFAULT_RECEIVER_USER_NAME = "receiver";
    public static final String DEFAULT_RECEIVER_USER_PASSWORD = "ostfalia";

    public static final String MQTT_MESSAGE_PUBLISHED = "MQTT messages was published.";
    public static final String MQTT_MESSAGE_NOT_CONNECTED = "MQTT mqtt is not connected.";
    public static final String MQTT_MESSAGE_ALREADY_CONNECTED = "MQTT mqtt is already connected.";
    public static final String MQTT_MESSAGE_CONNECTING = "MQTT mqtt is connecting to broker: %s.";
    public static final String MQTT_MESSAGE_CONNECTION_ESTABLISHED = "MQTT connection is established.";
    public static final String MQTT_MESSAGE_ATTEMPTING_CONNECT = "MQTT mqtt is attempting to connect...";
    public static final String MQTT_MESSAGE_DISCONNECTED = "MQTT mqtt is disconnected.";

    public static final String MQTT_MESSAGE_PUBLISHING_WITH_QOS = "Publishing messages {%s} to topic {%s} with QOS {%d}";
    public static final String CMD_KILL_STREAM = "pkill gst-launch-1.0";

    public static final int MQTT_RECONNECT_MILLIS = 1000;
    public static final int CLIENT_KEEP_ALIVE_SECONDS = 10; // 0 = disabled
    public static final int CLIENT_CONNECT_TIMEOUT_SECONDS = 20;
    public static final int VIDEO_QOS = 2;
    public static final int STEERING_QOS = 0;
    public static final int STOP_QOS = 1;
}

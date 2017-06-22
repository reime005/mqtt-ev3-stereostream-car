package server;

import de.ostfalia.remotecar.Constants;
import de.ostfalia.remotecar.mqtt.AbstractMqttSubscribeClient;
import de.ostfalia.remotecar.message.MotionControlMessage;
import de.ostfalia.remotecar.message.VideoControlMessage;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Created by Marius Reimer on 15.01.2017.
 */
public class MqttCarSubscribeClient extends AbstractMqttSubscribeClient {

    private IMessageAnalyzer messageAnalyzer;

    public MqttCarSubscribeClient() throws Exception {
        super();
    }

    public MqttCarSubscribeClient(String ip, int port, int qos) throws Exception {
        super(ip, port, qos);
    }

    public void init() throws Exception {
        messageAnalyzer = new MessageAnalyzer();
    }

    @Override
    public void disconnect() {
        super.disconnect();
        if (messageAnalyzer != null) {
            try {
                messageAnalyzer.close();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        if (topic == null) {
            return;
        }

        super.messageArrived(topic, message);

        if ((topic.equals(Constants.MQTT_TOPIC_STEERING_MESSAGE) || topic.equals(Constants.MQTT_TOPIC_STOP_MESSAGE)) && messageAnalyzer != null) {
            messageAnalyzer.analyzeAndControl(MotionControlMessage.asString(new String(message.getPayload())));
        } else if (topic.equals(Constants.MQTT_TOPIC_VIDEO_CONTROL_MESSAGE)) {
            handleVideoMessage(VideoControlMessage.asString(new String(message.getPayload())));
        }
    }

    private void handleVideoMessage(VideoControlMessage message) throws IOException, InterruptedException {
        if (message != null) {

            Runtime.getRuntime().exec(Constants.CMD_KILL_STREAM);

            Runtime.getRuntime().exec(new String[]{"sh", Constants.GST_LAUNCH_SCRIPT,
                    String.valueOf(message.getVideoWidth()),
                    String.valueOf(message.getVideoHeight()),
                    String.valueOf(message.getVideoFps()),
                    message.getUdpSinkIp(),
                    String.valueOf(message.getUdpSinkPort())});
        }
    }
}

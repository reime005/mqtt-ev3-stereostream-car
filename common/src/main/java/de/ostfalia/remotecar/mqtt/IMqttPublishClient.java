package de.ostfalia.remotecar.mqtt;

import de.ostfalia.remotecar.message.MotionControlMessage;
import de.ostfalia.remotecar.message.VideoControlMessage;

/**
 * Created by Marius Reimers Reimer on 05/03/17.
 */
public interface IMqttPublishClient {

    void sendMotionControlMsg(MotionControlMessage msg);

    void sendVideoControlMsg(VideoControlMessage msg);
}

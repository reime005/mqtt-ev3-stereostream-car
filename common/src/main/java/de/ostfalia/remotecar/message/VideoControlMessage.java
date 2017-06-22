package de.ostfalia.remotecar.message;

import java.io.Serializable;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Created by Marius Reimers Reimer on 16/02/17.
 */
public class VideoControlMessage implements Serializable {

    private final String udpSinkIp;
    private final int videoWidth;
    private final int videoHeight;
    private final int udpSinkPort;
    private final int videoFps;

    public VideoControlMessage(String udpSinkIp, int videoWidth, int videoHeight, int udpSinkPort, int videoFps) {
        this.udpSinkIp = udpSinkIp;
        this.videoWidth = videoWidth;
        this.videoHeight = videoHeight;
        this.udpSinkPort = udpSinkPort;
        this.videoFps = videoFps;
    }

    public String getUdpSinkIp() {
        return udpSinkIp;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public int getUdpSinkPort() {
        return udpSinkPort;
    }

    public int getVideoFps() {
        return videoFps;
    }

    /**
     * Parse from string (MQTT use case)
     * @param value toString() output
     * @return Object from the string, if succeeded
     * @throws ParseException if error happened
     */
    public static VideoControlMessage asString(String value) throws ParseException {
        String udpSinkIp = "";
        int udpPort = 0;
        int videoWidth = 0;
        int videoHeight = 0;
        int videoFps = 0;

        final String[] result = value.split(" ");

        Logger.getAnonymousLogger().info(Arrays.toString(result));

        try {
            udpPort = Integer.parseInt(result[0]);
            udpSinkIp = result[1];
            videoWidth = Integer.parseInt(result[2]);
            videoHeight = Integer.parseInt(result[3]);
            videoFps = Integer.parseInt(result[4]);
        } catch (Exception e) {
            throw new ParseException("Cannot Parse " + VideoControlMessage.class.getCanonicalName());
        }
        return new VideoControlMessage(udpSinkIp, videoWidth, videoHeight, udpPort, videoFps);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(udpSinkPort)
                .append(" ").append(udpSinkIp)
                .append(" ").append(videoWidth)
                .append(" ").append(videoHeight)
                .append(" ").append(videoFps);
        return sb.toString();
    }
}

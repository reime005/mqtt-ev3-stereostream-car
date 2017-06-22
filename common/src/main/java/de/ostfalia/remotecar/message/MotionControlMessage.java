package de.ostfalia.remotecar.message;

public class MotionControlMessage {

    private SpeedParameter speedParameter;
    private SteeringParameter steeringParameter;

    public MotionControlMessage(SpeedParameter speedParameter, SteeringParameter steeringParameter) {
        this.speedParameter = speedParameter;
        this.steeringParameter = steeringParameter;
    }

    /**
     * Parse from string (MQTT use case)
     * @param value toString() output
     * @return Object from the string, if succeeded
     * @throws ParseException if error happened
     */
    public static MotionControlMessage asString(String value) throws ParseException {
        final SpeedParameter speedParameter;
        final SteeringParameter steeringParameter;

        // value MUST be like "0 2"
        final String[] result = value.split(" ");
        try { // number of parameter we are sending in this messages
            speedParameter = SpeedParameter.values()[Integer.parseInt(result[0])];
            steeringParameter = SteeringParameter.values()[Integer.parseInt(result[1])];
        } catch (Exception e) {
            throw new ParseException("Cannot Parse " + MotionControlMessage.class.getCanonicalName());
        }

        return new MotionControlMessage(speedParameter, steeringParameter);
    }

    public SpeedParameter getSpeedParameter() {
        return speedParameter;
    }

    public SteeringParameter getSteeringParameter() {
        return steeringParameter;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(speedParameter.ordinal()).append(" ").append(steeringParameter.ordinal()).toString();
    }

    public enum SpeedParameter {
        FORWARD,
        BACKWARD,
        STOP
    }

    public enum SteeringParameter {
        LEFT,
        RIGHT,
        STRAIGHT
    }
}

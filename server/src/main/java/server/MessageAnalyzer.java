package server;

import de.ostfalia.remotecar.message.MotionControlMessage;

import java.rmi.RemoteException;
import java.util.logging.Logger;

/**
 * Created by Marius Reimers Reimer on 05/03/17.
 */
public class MessageAnalyzer implements IMessageAnalyzer {

    private IMotionController controller;
    private Logger logger;

    public MessageAnalyzer() throws Exception {
        controller = new MotionController();
        // close all ports to prevent opening errors on the next start
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                controller.closePorts();
            } catch (RemoteException e) {
                logger.warning(e.getMessage());
            }
        }));
        logger = Logger.getLogger(getClass().getCanonicalName());
    }

    @Override
    public void analyzeAndControl(MotionControlMessage msg) throws RemoteException {

        switch (msg.getSpeedParameter()) {
            case STOP:
                controller.stop();
                controller.steeringStraight();
                break;

            case BACKWARD:
                controller.driveBackward();
                break;

            case FORWARD:
                controller.driveForward();
                break;
        }

        switch (msg.getSteeringParameter()) {
            case STRAIGHT:
                controller.steeringStraight();
                break;

            case LEFT:
                controller.steeringLeft();
                break;

            case RIGHT:
                controller.steeringRight();
                break;
        }
    }

    @Override
    public void close() throws RemoteException {
        if (controller != null) {
            controller.closePorts();
        }
    }
}

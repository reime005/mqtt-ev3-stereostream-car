package server;

import de.ostfalia.remotecar.Constants;
import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;

import java.rmi.RemoteException;
import java.util.logging.Logger;

/**
 * Class to handle the drive engines on the car
 */
public class MotionController implements IMotionController {

    private Logger logger;
    private RemoteEV3 brick;

    private RMIRegulatedMotor steeringMotorA;
    private RMIRegulatedMotor driveMotorLeftB;
    private RMIRegulatedMotor driveMotorLeftC;

    public MotionController() throws Exception {
        init();
    }

    private void init() throws Exception {
        logger = Logger.getLogger(getClass().getCanonicalName());

        BrickInfo[] bricks = BrickFinder.discover();

        // try to discover the brick, until we found one
        do {
            try {
                while (bricks.length < 1) {
                    Thread.sleep(Constants.MQTT_RECONNECT_MILLIS);
                    logger.warning("No bricks found. Try to discover again...");
                    bricks = BrickFinder.discover();
                }

                // we just want the first brick, if there are more than one
                brick = new RemoteEV3(bricks[0].getIPAddress());
            } catch (Exception e) {
                logger.warning(e.getMessage());
                Thread.sleep(Constants.MQTT_RECONNECT_MILLIS);
            }
        } while (brick == null);

        brick.setDefault();

        // initialize remote motors
        steeringMotorA = brick.createRegulatedMotor("A", 'M');
        driveMotorLeftB = brick.createRegulatedMotor("B", 'L');
        driveMotorLeftC = brick.createRegulatedMotor("C", 'L');

        stop();
        driveMotorLeftB.setSpeed((int) driveMotorLeftB.getMaxSpeed());
        driveMotorLeftC.setSpeed((int) driveMotorLeftC.getMaxSpeed());
        steeringMotorA.setSpeed((int) steeringMotorA.getMaxSpeed());
    }

    @Override
    public void stop() throws RemoteException {
        // if parameter is true, method returns immediately and the motor stops by itself
        driveMotorLeftB.stop(true);
        driveMotorLeftC.stop(true);
    }

    @Override
    public void closePorts() throws RemoteException {
        steeringMotorA.close();
        driveMotorLeftB.close();
        driveMotorLeftC.close();
        logger.info("Ports are closed.");
    }

    @Override
    public void steeringLeft() throws RemoteException {
        steeringMotorA.rotateTo(-90, true);
    }

    @Override
    public void steeringRight() throws RemoteException {
        steeringMotorA.rotateTo(90, true);
    }

    @Override
    public void steeringStraight() throws RemoteException {
        steeringMotorA.rotateTo(0, true);
    }

    @Override
    public void driveBackward() throws RemoteException {
        driveMotorLeftB.backward();
        driveMotorLeftC.backward();
    }

    @Override
    public void driveForward() throws RemoteException {
        driveMotorLeftB.forward();
        driveMotorLeftC.forward();
    }
}

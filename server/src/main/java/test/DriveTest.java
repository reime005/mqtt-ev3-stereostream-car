package test;

import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;
import lejos.utility.Delay;

import java.rmi.RemoteException;
import java.util.logging.Logger;

public class DriveTest {

    private RemoteEV3 brick;
    private Logger logger;

    public DriveTest(RemoteEV3 remoteEV3) throws RemoteException {
        this.brick = remoteEV3;

        logger = Logger.getLogger(getClass().getCanonicalName());
        logger.info("initialized: " + brick.getName());

        RMIRegulatedMotor motorB = brick.createRegulatedMotor("B", 'L');
        RMIRegulatedMotor motorC = brick.createRegulatedMotor("C", 'L');

        RMIRegulatedMotor motorA = brick.createRegulatedMotor("A", 'M');

        logger.info("max speed: " + motorB.getMaxSpeed());

        motorA.setSpeed((int) motorA.getMaxSpeed());

        logger.info("current angle: " + motorA.getLimitAngle());

        motorA.rotateTo(0, true);
        logger.info("current angle: " + motorA.getLimitAngle());

        Delay.msDelay(2000);
        motorA.rotateTo(90, true); // right
        logger.info("current angle: " + motorA.getLimitAngle());

        Delay.msDelay(2000);
        motorA.rotateTo(-90, true); // left
        logger.info("current angle: " + motorA.getLimitAngle());

        Delay.msDelay(2000);
        motorA.rotateTo(0, false);
        logger.info("current angle: " + motorA.getLimitAngle());

        Delay.msDelay(2000);


        /*
        motorB.setSpeed((int)motorB.getMaxSpeed());
        motorC.setSpeed((int)motorB.getMaxSpeed());

        for (int i = 0; i < 2; i++) {
            motorB.forward();
            motorC.forward();
            Delay.msDelay(1000);
            motorB.stop(false);
            motorC.stop(false);
        }*/

        /*
        motorA.rotate(-90);

        Delay.msDelay(2000);

        motorA.rotate(180);

        Delay.msDelay(2000);

        motorA.rotate(-90);

        Delay.msDelay(2000);

        motorA.rotate(90);

        Delay.msDelay(2000);

        motorA.rotate(-180);

        Delay.msDelay(2000);

        motorA.rotate(90);



        logger.info("current angle: " + motorA.getLimitAngle());
        motorA.rotateTo(0, false);
        Delay.msDelay(2000);
        motorA.rotateTo(90, false); // right
        Delay.msDelay(2000);
        motorA.rotateTo(-90, false); // left
        Delay.msDelay(2000);
        motorA.rotateTo(0, false);
        logger.info("current angle: " + motorA.getLimitAngle());
 */

        motorA.stop(true);
        motorB.stop(true);
        motorC.stop(true);

        motorA.close();
        motorB.close();
        motorC.close();

        brick.setDefault();
    }

    public static void main(String[] args) throws Exception {
        BrickInfo[] bricks = BrickFinder.discover();

        for (BrickInfo info : bricks) {
            new DriveTest(new RemoteEV3(info.getIPAddress()));
        }
    }
}

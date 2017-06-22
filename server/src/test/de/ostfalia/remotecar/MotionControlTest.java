package de.ostfalia.remotecar;

import lejos.utility.Delay;
import org.junit.*;
import org.junit.runners.MethodSorters;
import server.IMotionController;
import server.MotionController;

import java.rmi.RemoteException;
import java.util.logging.Logger;

/**
 * Created by Marius Reimers on 15.03.17.
 */
@FixMethodOrder(MethodSorters.JVM)
public class MotionControlTest {

    private static IMotionController motionController;
    private static final int DELAY_MS = 2000;

    @BeforeClass
    public static void setUp() throws Exception {
        try {
            motionController = new MotionController();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    motionController.closePorts();
                    Logger.getAnonymousLogger().info("closed");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }));
        } catch (Exception e) {
            System.exit(0);
        }
    }

    @After
    public void delay() {
        Delay.msDelay(DELAY_MS);
    }

    @Before
    public void steeringStraight() throws RemoteException {
        motionController.steeringStraight();
    }

    @AfterClass
    public static void shutDown() throws Exception {
        motionController.steeringStraight();
        motionController.closePorts();
    }

    @Test
    public void driveForwardTest() throws RemoteException {
        motionController.driveForward();
    }

    @Test
    public void driveBackwardTest() throws RemoteException {
        motionController.driveBackward();
    }

    @Test
    public void driveRightBackwardTest() throws RemoteException {
        motionController.steeringRight();
        motionController.driveBackward();
    }

    @Test
    public void driveLeftBackwardTest() throws RemoteException {
        motionController.steeringLeft();
        motionController.driveBackward();
    }

    @Test
    public void driveRightForwardTest() throws RemoteException {
        motionController.steeringRight();
        motionController.driveForward();
    }

    @Test
    public void driveLeftForwardTest() throws RemoteException {
        motionController.steeringLeft();
        motionController.driveForward();
    }
}

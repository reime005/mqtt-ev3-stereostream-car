package server;

import java.rmi.RemoteException;

/**
 * Created by Marius Reimers Reimer on 05/03/17.
 */
public interface IMotionController {
    void stop() throws RemoteException;
    void closePorts() throws RemoteException;
    void steeringLeft() throws RemoteException;
    void steeringRight() throws RemoteException;
    void steeringStraight() throws RemoteException;
    void driveBackward() throws RemoteException;
    void driveForward() throws RemoteException;
}

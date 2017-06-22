package server;

import de.ostfalia.remotecar.message.MotionControlMessage;

import java.rmi.RemoteException;

/**
 * Created by Marius Reimers Reimer on 05/03/17.
 */
public interface IMessageAnalyzer {
    void analyzeAndControl(MotionControlMessage msg) throws RemoteException;
    void close() throws RemoteException;
}

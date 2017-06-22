package de.ostfalia.remotecar.mqtt;

import java.rmi.RemoteException;

/**
 * Interface to connect
 * Created by Marius Reimer on 29.11.2016.
 */
public interface ICarControlClient {
    void connect() throws Exception;

    void disconnect() throws RemoteException;

    boolean isConnected();
}

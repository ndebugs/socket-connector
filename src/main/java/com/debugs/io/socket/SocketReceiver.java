/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.io.socket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class SocketReceiver implements Runnable {

    private SocketConnection connection;
    private boolean started;

    public SocketReceiver(SocketConnection connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        Socket socket = connection.getSocket();
        SocketListener listener = connection.getListener();
        try {
            InputStream in = socket.getInputStream();
            started = true;
            boolean keepAlive = socket.getKeepAlive();
            int len;
            byte[] buff = new byte[socket.getReceiveBufferSize()];
            do {
                len = in.read(buff);
                if (len > 0) {
                    ByteArrayInputStream byteIn = new ByteArrayInputStream(buff, 0, len);
                    listener.onReceive(connection, byteIn);
                }
            } while (started && (keepAlive || len != -1));
            listener.onComplete(connection);
        } catch (IOException e) {
            listener.onError(connection, e);
        }
    }

    public void cancel() {
        started = false;
    }
}

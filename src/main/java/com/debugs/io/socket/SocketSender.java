/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.io.socket;

import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class SocketSender implements Runnable {

    private SocketConnection connection;
    private final LinkedList<byte[]> dataList = new LinkedList();
    private boolean started;

    SocketSender(SocketConnection connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        Socket socket = connection.getSocket();
        SocketListener listener = connection.getListener();
        try {
            OutputStream out = socket.getOutputStream();
            started = true;
            while (started) {
                synchronized (dataList) {
                    if (dataList.isEmpty()) {
                        dataList.wait();
                    } else {
                        byte[] data = dataList.removeFirst();
                        out.write(data);
                        out.flush();
                        listener.onSend(connection, data);
                    }
                }
            }
            listener.onComplete(connection);
        } catch (Exception e) {
            listener.onError(connection, e);
        }
    }

    public void send(byte[] data) {
        synchronized (dataList) {
            dataList.addLast(data);
            dataList.notify();
        }
    }

    public void cancel() {
        started = false;
        synchronized (dataList) {
            dataList.clear();
            dataList.notify();
        }
    }
}

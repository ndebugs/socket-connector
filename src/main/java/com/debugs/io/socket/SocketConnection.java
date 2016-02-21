/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.io.socket;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class SocketConnection {

    private Socket socket;
    private SocketListener listener;
    private ExecutorService executor;
    private SocketReceiver receiver;
    private SocketSender sender;
    private boolean started;
    
    public SocketConnection(Socket socket, SocketListener listener) {
        this(socket, listener, Executors.newCachedThreadPool());
    }
    
    public SocketConnection(Socket socket, SocketListener listener, ExecutorService executor) {
        this.socket = socket;
        this.listener = listener;
        this.executor = executor;
        receiver = new SocketReceiver(this);
        sender = new SocketSender(this);
    }
    
    public void open() {
        if (started) {
            return;
        }
        started = true;
        executor.execute(receiver);
        executor.execute(sender);
        listener.onOpen(this);
    }
    
    public void close() throws IOException {
        if (!started) {
            return;
        }
        receiver.cancel();
        sender.cancel();
        socket.close();
        started = false;
        listener.onClose(this);
    }

    public Socket getSocket() {
        return socket;
    }

    public SocketListener getListener() {
        return listener;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public SocketReceiver getReceiver() {
        return receiver;
    }
    
    public SocketSender getSender() {
        return sender;
    }
    
    public boolean isClosed() {
        return !started;
    }
}

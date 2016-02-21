/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.debugs.io.socket;

import java.io.ByteArrayInputStream;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public interface SocketListener {

    public void onOpen(SocketConnection conn);
    
    public void onClose(SocketConnection conn);
    
    public void onSend(SocketConnection conn, byte[] data);
    
    public void onReceive(SocketConnection conn, ByteArrayInputStream in);
    
    public void onComplete(SocketConnection conn);
    
    public void onError(SocketConnection conn, Throwable t);
}

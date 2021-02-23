package com.gproject.tool;

import android.util.Log;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class PrivacyWebSocket extends WebSocketClient {


    public PrivacyWebSocket(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.e("WebSocketClient", "onOpen()");
    }

    @Override
    public void onMessage(String message) {
        Log.e("WebSocketClient", "onMessage()");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.e("WebSocketClient", "onClose()");
    }

    @Override
    public void onError(Exception ex) {
        Log.e("WebSocketClient", "onError()");
    }
}

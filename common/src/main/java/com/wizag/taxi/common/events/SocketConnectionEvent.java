package com.wizag.taxi.common.events;

public class SocketConnectionEvent extends BaseResultEvent {
    public String event;
    public SocketConnectionEvent(String event) {
        super(200);
        this.event = event;
    }
}

package com.yechy.tvassserver.structure;

/**
 * Created by yechy on 2017/4/15.
 */

public class DeviceBean {
    private String ip;
    private int port;
    private String name;
    private String room;

    @Override
    public int hashCode() {
        return ip.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DeviceBean) {
            return this.ip.equals(((DeviceBean) obj).getIp());
        }
        return super.equals(obj);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}

package com.metinsaritas.phocom.Types;

public class Wifi {

    private String ssid;
    private String password;
    private String ip;

    public String getSsid() {
        return ssid;
    }

    public String getPassword() {
        return password;
    }

    public boolean verify() {
        return !(
                   ssid == null || ssid.length() <= 0
                || password == null || password.length() <= 0
                || ip == null || ip.length() <= 0
        );
    }

    public String getIp() {
        return ip;
    }
}

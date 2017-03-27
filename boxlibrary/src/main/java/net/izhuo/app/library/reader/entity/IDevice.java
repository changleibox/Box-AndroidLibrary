/**
 * Copyright © All right reserved by IZHUO.NET.
 */
package net.izhuo.app.library.reader.entity;

import android.text.TextUtils;

/**
 * @author Box
 * @since Jdk1.6
 * <p>
 * 2015年12月11日 上午11:02:24
 * 常用登录设备
 */
@SuppressWarnings("unused")
public class IDevice {

    /**
     * loginTime : 1466494763703
     * memorySize : 2
     * deviceTitle : cccxxx
     * deviceVersion : 1.0
     * id : xxcccxx
     * userName : 15600000005
     * deviceID : xxx
     * deviceName : xxxcc
     * systemVersion : 10.0
     */

    private long loginTime;
    private String memorySize;
    private String deviceTitle;
    private String deviceVersion;
    private String id;
    private String userName;
    private String deviceID;
    private String deviceName;
    private String systemVersion;

    private boolean using;

    public boolean isUsing() {
        return using;
    }

    public void setUsing(boolean using) {
        this.using = using;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public String getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(String memorySize) {
        this.memorySize = memorySize;
    }

    public String getDeviceTitle() {
        return deviceTitle;
    }

    public void setDeviceTitle(String deviceTitle) {
        this.deviceTitle = deviceTitle;
    }

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeviceID() {
        return TextUtils.isEmpty(deviceID) ? id : deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }
}

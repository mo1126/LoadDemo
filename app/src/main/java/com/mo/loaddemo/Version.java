package com.mo.loaddemo;

/**
 * Created by Mo on 2017/9/12.
 */

public class Version {
    private int VersionCode = 200;//版本号
    private String url;//远程apk地址


    public int getVersionCode() {
        return VersionCode;
    }

    public String getUrl() {
        return url;
    }

    public void setVersionCode(int versionCode) {
        VersionCode = versionCode;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

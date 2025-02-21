package com.sse.sseapp.proxy.qqxy.shaded;

/**
 * Created by admin on 2018/8/12.
 */

public class GetBaseRequest<T> {
    private String base;
    private String url;
    private T reqContent;

    public GetBaseRequest() {
        this.base = "";
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public T getReqContent() {
        return reqContent;
    }

    public void setReqContent(T reqContent) {
        this.reqContent = reqContent;
    }

    @Override
    public String toString() {
        return "GetBaseRequest{" +
                "base='" + base + '\'' +
                ", url='" + url + '\'' +
                ", reqContent=" + reqContent +
                '}';
    }
}



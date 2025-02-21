package com.sse.sseapp.form.request;


import lombok.Data;

@Data
public class BulletinReadCountReqBody {

    private String token = "APPMQUERY";
    private String docId;
}

package io.ocnet.android.devicefingerprint.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


public class RetrieveEntity implements Serializable {

    /*

     {
        "result":"success",
        "errors":[],
        "content":{"wine": "Gin"},
        "message":"message",
        "code":200,
        "messageArgs":[]
     }
     */
    @JsonProperty
    private String wine;

    @Override
    public String toString() {
        return "{ wine: " + wine
                + " }";
    }
}

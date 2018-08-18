package io.ocnet.blockchain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Entity implements Serializable {

    @JsonProperty
    public String jsonrpc;
    @JsonProperty
    public String id;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

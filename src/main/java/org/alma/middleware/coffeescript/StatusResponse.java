package org.alma.middleware.coffeescript;

/**
 * Created by Maxime on 04/12/2015.
 */
public class StatusResponse {
    public StatusResponse() {}

    private String status;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static StatusResponse getOK() {
        StatusResponse r = new StatusResponse();
        r.setStatus("ok");
        return r;
    }

    public static StatusResponse getKO() {
        StatusResponse r = new StatusResponse();
        r.setStatus("ko");
        return r;
    }
}

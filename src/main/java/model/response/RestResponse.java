package model.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;

public class RestResponse {
    @JsonProperty("message")
    @Size(max = 2048)
    private String message;

    @JsonProperty("domain")
    @Size(max = 64)
    private String domain;

    @JsonProperty("detailed")
    @Size(max = 4096)
    private String detailed;

    @JsonProperty("timestamp")
    @Size(max = 1024)
    private String timestamp;

    public RestResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDetailed() {
        return detailed;
    }

    public void setDetailed(String detailed) {
        this.detailed = detailed;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

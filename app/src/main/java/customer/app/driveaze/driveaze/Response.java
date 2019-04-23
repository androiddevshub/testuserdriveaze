package customer.app.driveaze.driveaze;

import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("code")
    String code;

    @SerializedName("message")
    String message;

    @SerializedName("auth-token")
    String auth_token;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }
}

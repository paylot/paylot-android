package co.paylot.android.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with love by Dozie on 10/24/2018.
 */

public class TxPayload {
    private String type = "payment";
    private String name;
    private String email;
    private String subject;
    private boolean sendMail;

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public TxPayload setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public TxPayload setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public TxPayload setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public boolean isSendMail() {
        return sendMail;
    }

    public TxPayload setSendMail(boolean sendMail) {
        this.sendMail = sendMail;
        return this;
    }

    public JSONObject toJSON(){
        JSONObject data = new JSONObject();
        try {
            data.putOpt("email", getEmail());
            data.putOpt("type", getType());
            data.putOpt("sendMail", isSendMail());
            data.putOpt("subject", getSubject());
        }catch (JSONException bug){
            bug.printStackTrace();
        }
        return data;
    }
}

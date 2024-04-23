package scenes;

import java.io.Serializable;
import java.util.HashMap;

public class Message implements Serializable {
    static final long serialVersionUID = 42L;
    HashMap<String, Object> payload;
    MessageType msgType;
    public Message(HashMap<String, Object> payload_, MessageType msgType_) {
        payload = payload_;
        msgType = msgType_;
    }
}

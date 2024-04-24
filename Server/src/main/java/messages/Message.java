package messages;

import controllers.GameStatus;

import java.io.Serializable;
import java.util.HashMap;

public class Message implements Serializable {
    static final long serialVersionUID = 42L;
    public HashMap<String, Object> payload = new HashMap<>();
    public MessageType msgType;
    public Message() {}

    public Message createLogMessage(String msg) {
        msgType = MessageType.Log;
        payload.put("Content", msg);
        return this;
    }

    public Message changeGameStatus(GameStatus status) {
        msgType = MessageType.GameStatusUpdate;
        payload.put("Status", status);
        return this;
    }
}

package messages;

import gamebackend.Ship;

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

    public Message createSceneSwitchMessage(String scene) {
        msgType = MessageType.SceneSwitch;
        payload.put("Scene", scene);
        return this;
    }

    public Message createStatusMessage(GameStatus status) {
        msgType = MessageType.GameStatusUpdate;
        payload.put("Status", status);
        return this;
    }

    public Message createShipPlacementMessage(Ship ship_, int x_, int y_) {
        msgType = MessageType.ShipPlacement;
        payload.put("Ship", ship_);
        payload.put("X", x_);
        payload.put("Y", y_);
        payload.put("Status", false);
        return this;
    }
}

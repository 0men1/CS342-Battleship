package scenes;

import messages.Message;

public interface BattleshipScene {
    public void render();
    public void handleMessage(Message msg);

    void handleMessage(Message msg);
}

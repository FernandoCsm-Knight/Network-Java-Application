package common.integration;

import common.messages.Message;

public interface ClientListener {
    public void onMessageReceived(Message message);
}

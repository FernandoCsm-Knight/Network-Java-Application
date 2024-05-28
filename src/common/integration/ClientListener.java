package common.integration;

import common.messages.Message;

public interface ClientListener<T extends Message> {
    public void onMessageReceived(T message);
}

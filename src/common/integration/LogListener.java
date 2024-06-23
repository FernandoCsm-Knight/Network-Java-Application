package common.integration;

import common.models.Log;

public interface LogListener {
    public void onLogReceived(Log log);
}

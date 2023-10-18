package com.example.springframe.websocket.task;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.TimerTask;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IotDataTimeTask extends TimerTask {

    private final String entityId;
    private final String cmdId;

    private String resultJson;

    public IotDataTimeTask(String entityId, String cmdId) {
        this.entityId = entityId;
        this.cmdId = cmdId;
    }

    @Override
    public void run() {

    }
}

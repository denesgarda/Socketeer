package com.denesgarda.Socketeer.data.event;

import com.denesgarda.Socketeer.data.Connection;
import com.denesgarda.Socketeer.data.DataType;

public abstract class ReceivedEvent extends Event {
    private final Connection connection;

    public ReceivedEvent(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public abstract Object read(DataType readDataType) throws Exception;

    public abstract void reply(Object object, DataType writeDataType) throws Exception;
}

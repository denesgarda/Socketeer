package com.denesgarda.Socketeer.data;

/**
 * This is the class that manages one-time actions within connections.
 * @author denesgarda
 */
public interface OneTimeAction {

    /**
     * The action method to be performed within a one-time connection
     * @param connection The Connection to be used within the action
     * @throws Exception
     */
    void action(Connection connection) throws Exception;
}

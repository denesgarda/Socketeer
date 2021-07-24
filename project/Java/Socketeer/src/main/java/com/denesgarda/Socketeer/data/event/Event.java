package com.denesgarda.Socketeer.data.event;

import java.lang.reflect.Method;

/**
 * This is the superclass of all events
 * @author denesgarda
 */
public class Event {
    /**
     * The constructor of Event
     */
    public Event() {

    }

    /**
     * This is the method used within the server to call various events
     * @param listener The listener to call the event to
     * @param event The event to be called
     */
    public static void callEvent(Listener listener, Event event) {
        for(Method method : listener.getClass().getMethods()) {
            if(method.isAnnotationPresent(EventHandler.class)) {
                for(Class c : method.getParameterTypes()) {
                    if(c.isInstance(event)) {
                        try {
                            method.invoke(listener, event);
                        }
                        catch (Exception ignored) {}
                    }
                }
            }
        }
    }
}

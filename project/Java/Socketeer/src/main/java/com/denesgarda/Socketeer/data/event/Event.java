package com.denesgarda.Socketeer.data.event;

import java.lang.reflect.Method;

/**
 * This is the superclass of all events.
 * @author denesgarda
 */
public class Event {

    /**
     * The constructor of Event.
     * This should only be used within socketeer. Users should not initialize events, as to not cause unnecessary confusion and Exceptions.
     */
    public Event() {

    }

    /**
     * This is the method used within socketeer to call various events.
     * This should only be used within socketeer. Users should not call events, as to not cause unnecessary confusion and Exceptions.
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

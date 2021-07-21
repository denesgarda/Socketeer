package com.denesgarda.Socketeer.data.event;

import java.lang.reflect.Method;

public class Event {
    public Event() {

    }

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

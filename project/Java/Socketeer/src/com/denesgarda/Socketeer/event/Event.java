package com.denesgarda.Socketeer.event;

import java.lang.reflect.Method;

public class Event {
    public Event() {

    }

    public static void callEvent(EventListener eventListener, Event event) {
        for(Method method : eventListener.getClass().getMethods()) {
            if(method.isAnnotationPresent(EventHandler.class)) {
                for(Class c : method.getParameterTypes()) {
                    if(c.isInstance(event)) {
                        try {
                            method.invoke(eventListener, event);
                        }
                        catch (Exception ignored) {}
                    }
                }
            }
        }
    }
}

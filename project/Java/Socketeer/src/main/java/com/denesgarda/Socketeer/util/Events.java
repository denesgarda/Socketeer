package com.denesgarda.Socketeer.util;

import com.denesgarda.Socketeer.data.End;
import com.denesgarda.Socketeer.data.event.Event;
import com.denesgarda.Socketeer.data.event.EventHandler;

import java.lang.reflect.Method;
import java.util.Arrays;

public class Events {
    public static void callEvent(End listener, Event event) {
        for(Method method : listener.getClass().getMethods()) {
            if(method.isAnnotationPresent(EventHandler.class)) {
                if(Arrays.asList(method.getParameterTypes()).contains(event.getClass())) {
                    try {
                        method.invoke(listener, event);
                    }
                    catch (Exception ignored) {}
                }
            }
        }
    }
}

package com.denesgarda.Socketeer.data.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This is the tag that should be used when declaring an event listener.
 * It is used to differentiate between normal methods and event listener methods.
 * If a method that has correct formatting for an event isn't working, it might be because is isn't tagged with @EventHandler.
 * @author denesgarda
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
}

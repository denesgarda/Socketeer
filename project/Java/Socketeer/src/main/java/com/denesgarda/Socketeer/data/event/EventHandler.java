package com.denesgarda.Socketeer.data.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This is the tag that should be used when declaring an event listener
 * It is used to differentiate between normal methods and event listener methods
 * @author denesgarda
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
}

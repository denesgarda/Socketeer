package com.denesgarda.Socketeer;

import java.io.IOException;

public interface Queueable {
    void nextIn(String data) throws IOException;
}

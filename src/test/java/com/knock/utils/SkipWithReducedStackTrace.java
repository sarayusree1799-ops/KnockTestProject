package com.knock.utils;

import org.testng.SkipException;

public class SkipWithReducedStackTrace extends SkipException {
    public SkipWithReducedStackTrace(String skipMessage) {
        super(skipMessage);
        this.reduceStackTrace();
    }
}
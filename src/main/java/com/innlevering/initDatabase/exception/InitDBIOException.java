package com.innlevering.initDatabase.exception;

import java.io.IOException;

/**
 * Main IOException class for InitDB
 * Created by hakonschutt on 18/11/2017.
 */
public class InitDBIOException extends IOException {
    public InitDBIOException(String message) {
        super(message);
    }
}

package com.innlevering.initDatabase.exception;

import java.io.FileNotFoundException;

/**
 * Created by hakonschutt on 18/11/2017.
 */
public class InitDBFileNotFoundException extends FileNotFoundException {
    public InitDBFileNotFoundException(String message) { super(message); }
}

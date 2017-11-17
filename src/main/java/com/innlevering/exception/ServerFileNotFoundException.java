package com.innlevering.exception;

import java.io.FileNotFoundException;

/**
 * Created by hakonschutt on 17/11/2017.
 */
public class ServerFileNotFoundException extends FileNotFoundException {
    public ServerFileNotFoundException() { super("Unable to locate property file. Make sure its not deleted."); }
}

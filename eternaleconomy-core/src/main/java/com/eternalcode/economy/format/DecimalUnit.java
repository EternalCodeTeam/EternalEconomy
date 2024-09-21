package com.eternalcode.economy.format;

import java.io.Serializable;

@SuppressWarnings("ClassCanBeRecord")
public class DecimalUnit implements Serializable {

    private final long factor;
    private final char suffix;

    public DecimalUnit(long factor, char suffix) {
        this.factor = factor;
        this.suffix = suffix;
    }

    public long getFactor() {
        return this.factor;
    }

    public char getSuffix() {
        return this.suffix;
    }
}

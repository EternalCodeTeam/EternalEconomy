package com.eternalcode.economy.format;

import java.io.Serializable;

public class EconomyUnit implements Serializable {

    private final long factor;
    private final char suffix;

    public EconomyUnit(long factor, char suffix) {
        this.factor = factor;
        this.suffix = suffix;
    }

    public long getFactor() {
        return factor;
    }

    public char getSuffix() {
        return suffix;
    }

}
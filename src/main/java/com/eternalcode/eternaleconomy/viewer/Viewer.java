package com.eternalcode.eternaleconomy.viewer;

import java.util.UUID;

public interface Viewer {

    UUID getUniqueId();

    boolean isConsole();

    String getName();

}

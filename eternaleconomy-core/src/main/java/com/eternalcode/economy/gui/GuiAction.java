package com.eternalcode.economy.gui;

/**
 * Actions available for GUI items.
 */
public enum GuiAction {

    /** No action */
    NONE,

    /** Close GUI */
    CLOSE,

    /** Execute command as player */
    COMMAND,

    /** Execute command as console */
    CONSOLE_COMMAND,

    /** Go to next page (pagination) */
    NEXT_PAGE,

    /** Go to previous page (pagination) */
    PREVIOUS_PAGE,

    /** Refresh current GUI */
    REFRESH
}

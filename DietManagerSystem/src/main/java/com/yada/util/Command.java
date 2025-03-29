package com.yada.util;

/**
 * Interface for command pattern.
 */
public interface Command {
    /**
     * Execute the command.
     */
    void execute();
    
    /**
     * Undo the command.
     */
    void undo();
}
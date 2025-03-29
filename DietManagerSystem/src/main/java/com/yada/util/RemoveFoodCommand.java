package com.yada.util;

import com.yada.model.DailyLog;
import com.yada.model.LogEntry;

import java.time.LocalDate;

/**
 * Command to remove a food from the daily log.
 */
public class RemoveFoodCommand implements Command {
    private DailyLog dailyLog;
    private LocalDate date;
    private LogEntry entry;
    
    /**
     * Constructor for RemoveFoodCommand.
     * 
     * @param dailyLog The daily log
     * @param date The date
     * @param entry The log entry to remove
     */
    public RemoveFoodCommand(DailyLog dailyLog, LocalDate date, LogEntry entry) {
        this.dailyLog = dailyLog;
        this.date = date;
        this.entry = entry;
    }
    
    /**
     * Execute the command to remove the food.
     */
    @Override
    public void execute() {
        dailyLog.removeEntry(date, entry);
        dailyLog.save();
    }
    
    /**
     * Undo the command by adding the food back.
     */
    @Override
    public void undo() {
        dailyLog.addEntry(date, entry);
        dailyLog.save();
    }
}
package com.treacher.butlerplankmaker.enums;

/**
 * Created by Michael Treacher
 */

public enum LogType {

    Normal(1511,1512, 960,100),
    Oak(1521,1522, 8778,250),
    Teak(6333,6334,8780,500),
    Mahogany(6332,8836,8782,1500);

    private int logId, notedLogId, plankId;
    private double sawmillCost;

    LogType(int logId, int notedLogId, int plankId, double sawmillCost) {
        this.logId = logId;
        this.notedLogId = notedLogId;
        this.plankId = plankId;
        this.sawmillCost = sawmillCost;
    }

    public int getLogId(){
        return logId;
    }

    public int getNotedLogId(){
        return notedLogId;
    }

    public int getPlankId(){
        return plankId;
    }

    public double getSawmillCost(){
        return sawmillCost;
    }

    public static LogType findById(int id){
        return LogType.values()[id];
    }
}

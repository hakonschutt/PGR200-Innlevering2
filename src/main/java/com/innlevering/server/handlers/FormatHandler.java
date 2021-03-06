package com.innlevering.server.handlers;

/**
 * Keeps track of the formates in the given tables
 * Created by hakonschutt on 23/10/2017.
 */
public enum FormatHandler {
    DAY_TEACHER_UNAVAILABILITY("| %-5s| %-12s| %-10s|"),
    FIELD_OF_STUDY("| %-10s| %-35s|"),
    POSSIBLE_DAY("| %-8s| %-15s|"),
    ROOM("| %-9s| %-15s|"),
    STUDY_SUBJECT("| %-5s| %-9s| %-15s|"),
    SUBJECT("| %-12s| %-40s| %-20s|"),
    TEACHER("| %-5s| %-27s| %-35s|"),
    TEACHER_SUBJECT("| %-5s| %-13s| %-10s|"),
    SEMESTER_PLAN("| %-5S| %-5S| %-8S| %-8S| %-12S| %-12S|");

    private String formatString;

    FormatHandler(String formatString) {
        this.formatString = formatString;
    }

    /**
     * Returns the enum string formatString.
     * @return
     */
    public String formatString() { return formatString; }

    /**
     * Returns the string format of the given table
     * @param tableName
     * @return
     */
    public static String getFormatFromHandler(String tableName){
        switch (tableName ){
            case "day_teacher_unavailability":
                return DAY_TEACHER_UNAVAILABILITY.formatString();
            case "field_of_study":
                return FIELD_OF_STUDY.formatString();
            case "possible_day":
                return POSSIBLE_DAY.formatString();
            case "room":
                return ROOM.formatString();
            case "study_subject":
                return STUDY_SUBJECT.formatString();
            case "subject":
                return SUBJECT.formatString();
            case "teacher":
                return TEACHER.formatString();
            case "teacher_subject":
                return TEACHER_SUBJECT.formatString();
            case "semester_plan":
                return SEMESTER_PLAN.formatString();
        }
        return null;
    }
}

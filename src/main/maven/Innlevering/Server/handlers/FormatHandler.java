package Innlevering.Server.handlers;

/**
 * Created by hakonschutt on 23/10/2017.
 */
public class FormatHandler {
    private final String day_teacher_unavailability;
    private final String field_of_study;
    private final String possible_day;
    private final String room;
    private final String study_subject;
    private final String subject;
    private final String teacher;
    private final String teacher_subject;

    public FormatHandler() {
        day_teacher_unavailability = "%-5s %-12s %-10s";
        field_of_study = "%-10s %-35s";
        possible_day = "%-8s %-15s";
        room = "%-9s %-15s";
        study_subject = "%-5s %-9s %-15s";
        subject = "%-12s %-40s %-20s";
        teacher = "%-5s %-27s %-35s";
        teacher_subject = "%-5s %-13s %-10s";
    }

    public String getDay_teacher_unavailability() {
        return day_teacher_unavailability;
    }
    public String getField_of_study() {
        return field_of_study;
    }
    public String getPossible_day() {
        return possible_day;
    }
    public String getRoom() {
        return room;
    }
    public String getStudy_subject() {
        return study_subject;
    }
    public String getSubject() {
        return subject;
    }
    public String getTeacher() {
        return teacher;
    }
    public String getTeacher_subject() {
        return teacher_subject;
    }

    public String getFormatFromHandler(String tableName){
        switch (tableName ){
            case "day_teacher_unavailability":
                return getDay_teacher_unavailability();
            case "field_of_study":
                return getField_of_study();
            case "possible_day":
                return getPossible_day();
            case "room":
                return getRoom();
            case "study_subject":
                return getStudy_subject();
            case "subject":
                return getSubject();
            case "teacher":
                return getTeacher();
            case "teacher_subject":
                return getTeacher_subject();
        }
        return null;
    }
}

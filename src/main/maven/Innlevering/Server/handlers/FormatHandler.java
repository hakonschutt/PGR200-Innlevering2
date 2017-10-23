package Innlevering.Server.handlers;

/**
 * Created by hakonschutt on 23/10/2017.
 */
public class FormatHandler {
    private String day_teacher_unavailability;
    private String field_of_study;
    private String possible_day;
    private String room;
    private String study_subject;
    private String subject;
    private String teacher;
    private String teacher_subject;

    public FormatHandler() {
        day_teacher_unavailability = "random";
        field_of_study = "random";
        possible_day = "random";
        room = "random";
        study_subject = "random";
        subject = "random";
        teacher = "random";
        teacher_subject = "random";
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
}

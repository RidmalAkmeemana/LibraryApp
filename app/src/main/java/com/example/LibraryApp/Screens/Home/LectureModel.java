package com.example.LibraryApp.Screens.Home;

public class LectureModel {

    private int lec_id;
    private String m_code;
    private String m_name;
    private String m_description;
    private String instructor_name;
    private String time_start;
    private String time_end;
    private String expired;

    public int getLec_id() {
        return lec_id;
    }

    public String getM_code() {
        return m_code;
    }

    public String getM_name() {
        return m_name;
    }

    public String getM_description() {
        return m_description;
    }

    public String getInstructor_name() {
        return instructor_name;
    }

    public String getTime_start() {
        return time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public String getExpired() {
        return expired;
    }
}

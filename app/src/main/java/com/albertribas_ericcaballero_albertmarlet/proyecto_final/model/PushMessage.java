package com.albertribas_ericcaballero_albertmarlet.proyecto_final.model;

/**
 * Created by albertribgar on 25/05/2016.
 */
public class PushMessage {

    private String subject;
    private String content;
    private String calendar;

    public PushMessage(String subject, String content, String cal){
        setSubject(subject);
        setContent(content);
        setCalendar(cal);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCalendar() {
        return calendar;
    }

    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }
}

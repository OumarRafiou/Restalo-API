package ca.ulaval.glo2003.domain;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

public class TimeDuration {
    private String from;
    private String to;
    private String open;
    private String close;

    public TimeDuration(String start, String finish) {
        this.open = start;
        this.from = start;
        this.close = finish;
        this.to = finish;
    }
    public TimeDuration() {
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getOpen() {
        return open;
    }

    public String getClose() {
        return  close;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public void setClose(String close) {
        this.close = close;
    }
    private void ValidateTimeFormat(String time) {
        try {
            LocalTime.parse(time);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Wrong time format");
        }
    }

    public boolean containsFrom() {
        return this.from != null;
    }

    public boolean containsTo() {
        return this.to != null;
    }
}

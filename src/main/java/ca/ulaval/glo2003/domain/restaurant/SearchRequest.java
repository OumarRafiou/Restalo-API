package ca.ulaval.glo2003.domain.restaurant;

import ca.ulaval.glo2003.domain.TimeDuration;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

public class SearchRequest {
    private String name;
    private Map<String, String> opened;

    public SearchRequest() {}

    public SearchRequest(String name, Map<String, String> opened) {
        this.name = name;
        this.opened = opened;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOpened(Map<String, String> opened) {
        this.opened = opened;
    }

    public TimeDuration getTime() {
        return new TimeDuration(this.opened.get("from"), this.opened.get("to"));
    }

    public void validate() {
        if (this.opened != null) {
            if (!this.isValidOpeningHours()) throw new IllegalArgumentException(
                "Wrong format of time"
            );
        }
    }

    public boolean isValidOpeningHours() {
        boolean validation = true;
        if (this.getTime().containsFrom()) {
            this.ValidateTimeFormat(this.getTime().getFrom());
            LocalTime open = LocalTime.parse(this.getTime().getFrom());
            validation =
                open.isAfter(LocalTime.MIDNIGHT) ||
                open.equals(LocalTime.MIDNIGHT);
        }
        if (this.getTime().containsTo()) {
            this.ValidateTimeFormat(this.getTime().getTo());
            LocalTime close = LocalTime.parse(this.getTime().getTo());

            boolean isClosingBeforeMidnight = close.isBefore(
                LocalTime.of(23, 59, 59)
            );
            validation = validation && isClosingBeforeMidnight;
        }
        return validation;
    }

    private void ValidateTimeFormat(String time) {
        try {
            LocalTime.parse(time);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Wrong time format");
        }
    }

    public boolean verificationName(String restaurantName) {
        boolean valid = true;
        if (this.name != null) {
            if (
                !restaurantName
                    .toLowerCase()
                    .replaceAll("\\s", "")
                    .contains(this.name.toLowerCase().replaceAll("\\s", ""))
            ) {
                valid = false;
            }
        }
        return valid;
    }

    public boolean verificationTime(Map<String, String> restaurantTime) {
        boolean valid = true;
        if (this.opened != null) {
            LocalTime closing = LocalTime.parse(restaurantTime.get("close"));
            LocalTime opening = LocalTime.parse(restaurantTime.get("open"));
            if (this.opened.get("from") != null) {
                LocalTime open = LocalTime.parse(this.opened.get("from"));
                if (!open.isBefore(closing)) {
                    valid = false;
                }
            }
            if (this.getTime().getTo() != null) {
                LocalTime close = LocalTime.parse(this.getTime().getTo());
                if (closing.isBefore(close) || !opening.isBefore(close)) {
                    valid = false;
                }
            }
        }
        return valid;
    }
}

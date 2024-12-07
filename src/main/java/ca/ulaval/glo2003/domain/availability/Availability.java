package ca.ulaval.glo2003.domain.availability;

public class Availability {
    private String start;
    private Integer remainingPlaces;

    public Availability(String start, Integer remainingPlaces) {
        this.start = start;
        this.remainingPlaces = remainingPlaces;
    }

    public Availability() {

    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public Integer getRemainingPlaces() {
        return remainingPlaces;
    }

    public void setRemainingPlaces(Integer remainingPlaces) {
        this.remainingPlaces = remainingPlaces;
    }
}

package ca.ulaval.glo2003.controllers;

import ca.ulaval.glo2003.controllers.assembler.ReservationAssembler;
import ca.ulaval.glo2003.controllers.assembler.RestaurantAssembler;
import ca.ulaval.glo2003.controllers.dto.ReservationDto;
import ca.ulaval.glo2003.controllers.dto.RestaurantDto;
import ca.ulaval.glo2003.domain.reservation.Reservation;
import ca.ulaval.glo2003.domain.reservation.TimeReservation;
import ca.ulaval.glo2003.domain.reservation.ReservationPersistence;
import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import ca.ulaval.glo2003.domain.restaurant.RestaurantPersistence;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationCtrl {
    private ReservationAssembler reservationAssembler;
    private RestaurantAssembler restaurantAssembler;
    private ReservationPersistence reservationPersistence;
    private RestaurantPersistence restaurantPersistence;
    public ReservationCtrl(
        ReservationAssembler reservationAssembler,
        RestaurantAssembler restaurantAssembler,
        ReservationPersistence reservationPersistence,
        RestaurantPersistence restaurantPersistence
    ) {
        this.reservationAssembler = reservationAssembler;
        this.restaurantAssembler = restaurantAssembler;
        this.reservationPersistence = reservationPersistence;
        this.restaurantPersistence = restaurantPersistence;
    }

    public String createReservation(
            Restaurant restaurantSelected,
            Reservation newReservation
    ) {
        String URIReservation = "reservations/" + newReservation.getId();
        newReservation.setRestaurant(restaurantSelected);
        newReservation.validate();
        if (!this.isNumberReservationValid(restaurantSelected, newReservation)) {
            throw new IllegalArgumentException("The capacity is overloaded");
        }

        reservationPersistence.save(newReservation);
        return URIReservation;
    }

    public ReservationDto getReservationFormatByNumber(String number) {
        try {
            Reservation reservation = getReservationByNumber(number);
            Restaurant restaurant = restaurantPersistence.getRestaurantById(
                reservation.getRestaurantId()
            );
            RestaurantDto restaurantDto = restaurantAssembler.toDto(restaurant);
            return reservationAssembler.toDto(reservation, restaurantDto);
        } catch (NotFoundException e) {
            throw new NotFoundException(
                    "error : getReservationFormatByNumber\n" + e.getMessage()
            );
        }
    }

    private Reservation getReservationByNumber(String number) {
        try {
            for (Reservation reservation : reservationPersistence.listAll()) {
                if (reservation.getId().equals(number)) {
                    return reservation;
                }
            }
            throw new NotFoundException("Reservation not found");
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }


    private boolean isNumberReservationValid(Restaurant restaurantselected, Reservation NewReservation) {
        List<Reservation> restaurantReservation = this.getRestaurantReservations(restaurantselected);
        List<TimeReservation> sameDateReservation = this.getReservationInSameDate(NewReservation, restaurantReservation);
        List<TimeReservation> sameTimeReservation = this.getReservationInSameTime(NewReservation, sameDateReservation);
        int capacity = restaurantselected.getCapacity();
        return this.isSurpassCapacity(new TimeReservation(NewReservation), sameTimeReservation, capacity);
    }

    private List<TimeReservation> getReservationInSameTime(
            Reservation baseReservation, List<TimeReservation> restaurantReservation) {
        List<TimeReservation> restaurantTimeReservation = new ArrayList<>();
        TimeReservation baseTimeReservation = new TimeReservation(baseReservation);
        LocalTime start = LocalTime.parse(baseTimeReservation.getStart());
        LocalTime end = LocalTime.parse(baseTimeReservation.getEnd());
        for (TimeReservation oldReservation : restaurantReservation) {
            boolean test = start.isBefore(LocalTime.parse(oldReservation.getEnd()));
            boolean test2 = end.isAfter(LocalTime.parse(oldReservation.getStart()));
            if (test && test2) {
                restaurantTimeReservation.add(oldReservation);
            }
        }
        return restaurantTimeReservation;
    }

    private List<TimeReservation> getReservationInSameDate(
            Reservation baseReservation, List<Reservation> restaurantReservation) {
        List<TimeReservation> restaurantTimeReservation = new ArrayList<>();
        for (Reservation oldReservation : restaurantReservation) {
            if (oldReservation.getDate().equals(baseReservation.getDate())) {
                restaurantTimeReservation.add(new TimeReservation(oldReservation));
            }
        }
        return restaurantTimeReservation;
    }

    private List<Reservation> getRestaurantReservations(Restaurant restaurantSelected) {
        List<Reservation> restaurantReservation = new ArrayList<>();
        for (Reservation oldReservation : reservationPersistence.listAll()) {
            if (oldReservation.getRestaurantId().equals(restaurantSelected.getId())) {
                restaurantReservation.add(oldReservation);
            }
        }
        return restaurantReservation;
    }

    private boolean isSurpassCapacity(TimeReservation baseReservation, List<TimeReservation> restaurantReservation, int capacity) {
        LocalTime startTime = LocalTime.parse(baseReservation.getStart());
        LocalTime stopTime = LocalTime.parse(baseReservation.getEnd());
        List<TimeReservation> copy = new ArrayList<>(restaurantReservation);
        boolean verification = true;
        boolean turn = true;
        while (!copy.isEmpty() || turn) {
            LocalTime minimum = startTime;
            turn = false;
            List<TimeReservation> copy2 = new ArrayList<>(copy);
            int numberAtSameTime = baseReservation.getGroupSize();

            for (TimeReservation others : copy2) {
                if (LocalTime.parse(others.getStart()).isBefore(startTime) ||
                        LocalTime.parse(others.getStart()).equals(startTime)) {
                    numberAtSameTime = numberAtSameTime + others.getGroupSize();
                }
                if (LocalTime.parse(others.getEnd()).isBefore(startTime)) {
                    numberAtSameTime = numberAtSameTime - others.getGroupSize();
                    copy.remove(others);
                }

            }
            startTime = this.getLowestTime(copy, minimum);
            if (numberAtSameTime > capacity) {
                verification = false;
                copy.clear();
            }
        }
        return verification;
    }

    private LocalTime getLowestTime(List<TimeReservation> restaurantReservation, LocalTime minimum) {
        LocalTime lowest = LocalTime.parse("23:59:59");
        for (TimeReservation others : restaurantReservation) {
            if (LocalTime.parse(others.getStart()).isBefore(lowest) &&
                    LocalTime.parse(others.getStart()).isAfter(minimum)) {
                lowest = LocalTime.parse(others.getStart());
            }
        }
        return lowest;
    }
    public void deleteReservation(Restaurant restaurantSelected, String reservationNumber) {
        reservationPersistence.delete(reservationNumber);
    }

    public List<Reservation> getReservationsForTime(String restaurantId, LocalTime intervalStart, LocalDate date) {
        return reservationPersistence.listAll().stream()
                .filter(reservation -> reservation.getRestaurantId().equals(restaurantId))
                .filter(reservation -> {
                    LocalTime reservationStartTime = LocalTime.parse(reservation.getStartTime());
                    LocalTime reservationEndTime = reservationStartTime.plusMinutes(restaurantPersistence.getRestaurantById(restaurantId).getReservationsDuration().get("duration"));
                    return (reservationStartTime.isBefore(intervalStart.plusMinutes(15)) && reservationEndTime.isAfter(intervalStart))
                            || (reservationStartTime.isAfter(intervalStart) && reservationStartTime.isBefore(intervalStart.plusMinutes(15)));
                })
                .filter(reservation -> reservation.getDate().equals(date.toString()))
                .collect(Collectors.toList());
    }


    public List<ReservationDto> searchReservations(
            String ownerId,
            String restaurantId,
            String date,
            String customerName
    ) {
        for (Restaurant restaurant : restaurantPersistence.listAll()) {
            if (restaurant.getId().equals(restaurantId)) {
                if (restaurant.getOwner().equals(ownerId)) {
                    List<Reservation> allReservations = reservationPersistence.listAll();

                    List<Reservation> restaurantReservations = allReservations.stream()
                            .filter(reservation -> reservation.getRestaurantId().equals(restaurantId))
                            .collect(Collectors.toList());

                    if (date != null && !date.isEmpty()) {
                        restaurantReservations = restaurantReservations.stream()
                                .filter(reservation -> reservation.getDate().equals(date))
                                .collect(Collectors.toList());
                    }

                    if (customerName != null && !customerName.isEmpty()) {
                        String trimmedCustomerName = customerName.trim().toLowerCase();
                        restaurantReservations = restaurantReservations.stream()
                                .filter(reservation ->
                                        reservation.getCustomer().getName().toLowerCase().startsWith(trimmedCustomerName))
                                .collect(Collectors.toList());
                    }

                    List<ReservationDto> reservationDtos = new ArrayList<>();
                    for (Reservation reservation : restaurantReservations) {
                        RestaurantDto restaurantDto = restaurantAssembler.toDto(restaurant);
                        reservationDtos.add(reservationAssembler.toDto(reservation, restaurantDto));
                    }

                    return reservationDtos;
                } else {
                    throw new NotFoundException(
                            "Restaurant not associated with Owner: " + ownerId
                    );
                }
            }
        }

        throw new NotFoundException(
                "This Restaurant does not exist: " + restaurantId
        );
    }


}

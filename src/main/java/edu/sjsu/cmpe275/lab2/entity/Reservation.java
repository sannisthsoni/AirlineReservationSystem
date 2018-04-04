package edu.sjsu.cmpe275.lab2.entity;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.GenericGenerator;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "reservationNumber")
//@JsonIgnoreProperties(allowSetters = true, value = { "passenger" })
@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "reservation_number", nullable = false)
    private String reservationNumber;

    private double price; // sum of each flight’s price.

//    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "passenger_id", nullable = false)
    private Passenger passenger;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "flight_id", nullable = false)
    private List<Flight> flights;

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    public JSONObject getReservationJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("reservationNumber", this.getReservationNumber());
        jsonObject.put("price", this.getPrice());
        jsonObject.put("passenger",this.getPassenger().getPassengerJSON());
        JSONObject jsonObjectFlights = new JSONObject();
        jsonObject.put("flights", jsonObjectFlights);
        JSONArray flightsJsonArray = new JSONArray();
        this.getFlights().forEach(flight -> {
            flightsJsonArray.put(flight.getFlightJSON());
        });
        jsonObjectFlights.put("flight",flightsJsonArray);
        return jsonObject;
    }

    public JSONObject getWholeReservationDetailsJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("reservation", this.getReservationJSON());
        return jsonObject;
    }
}

package rus.app.keepmoving.Entities;

import java.util.List;

public class Trip {
    private String departure_date;
    private String from_place;
    private String where_place;

    private String car_model;
    private String car_number;
    private String description;

    private String creator_id;
    private String trip_status;
    private List<String> requests;

    public Trip() {

    }

    public Trip(String departure_date, String from_place, String where_place,
                String description, String car_model, String car_number, String creator_id,
                String trip_status) {
        this.departure_date = departure_date;
        this.from_place = from_place;
        this.where_place = where_place;
        this.description = description;
        this.car_model = car_model;
        this.car_number = car_number;
        this.creator_id = creator_id;
        this.trip_status = trip_status;
    }

    public String getDeparture_date() {
        return departure_date;
    }

    public void setDeparture_date(String departure_date) {
        this.departure_date = departure_date;
    }

    public String getFrom_place() {
        return from_place;
    }

    public void setFrom_place(String from_place) {
        this.from_place = from_place;
    }

    public String getWhere_place() {
        return where_place;
    }

    public void setWhere_place(String where_place) {
        this.where_place = where_place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCar_model() {
        return car_model;
    }

    public void setCar_model(String car_model) {
        this.car_model = car_model;
    }

    public String getCar_number() {
        return car_number;
    }

    public void setCar_number(String car_number) {
        this.car_number = car_number;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public String getTrip_status() {
        return trip_status;
    }

    public void setTrip_status(String trip_status) {
        this.trip_status = trip_status;
    }

    public List<String> getRequests() {
        return requests;
    }

    public void setRequests(List<String> requests) {
        this.requests = requests;
    }
}

package rus.app.keepmoving.Entities;

public class TripListInfo {
    private String trip_id;
    private String from_place;
    private String where_place;
    private String departure_date;

    private String car_model;
    private String creator_name;

    public TripListInfo() {
    }

    public TripListInfo(String trip_id, String departure_date, String from_place, String where_place,
                        String car_model, String creator_name) {
        this.trip_id = trip_id;
        this.departure_date = departure_date;
        this.from_place = from_place;
        this.where_place = where_place;
        this.car_model = car_model;
        this.creator_name = creator_name;
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

    public String getCar_model() {
        return car_model;
    }

    public void setCar_model(String car_model) {
        this.car_model = car_model;
    }

    public String getCreator_name() {
        return creator_name;
    }

    public void setCreator_name(String creator_name) {
        this.creator_name = creator_name;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }
}

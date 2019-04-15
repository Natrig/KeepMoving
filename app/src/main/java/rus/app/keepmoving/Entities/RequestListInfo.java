package rus.app.keepmoving.Entities;

public class RequestListInfo {
    private String trip_id;
    private String user_id;
    private String user_name;
    private String imgUrl;

    public RequestListInfo() {
    }

    public RequestListInfo(String trip_id, String user_id, String user_name) {
        this.trip_id = trip_id;
        this.user_id = user_id;
        this.user_name = user_name;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}

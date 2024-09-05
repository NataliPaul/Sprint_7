package Client;

public class OrderList {

    private Integer  courierId;
    private String nearestStation;
    private Integer  limit;
    private Integer  page;

    public OrderList(Integer  courierId, String nearestStation, Integer  limit, Integer  page) {
        this.courierId = courierId;
        this.nearestStation = nearestStation;
        this.limit = limit;
        this.page = page;
    }

    public Integer  getCourierId() {
        return courierId;
    }

    public String getNearestStation() {
        return nearestStation;
    }

    public Integer  getLimit() {
        return limit;
    }

    public Integer  getPage() {
        return page;
    }

    public void setCourierId(Integer  courierId) {
        this.courierId = courierId;
    }

    public void setNearestStation(String nearestStation) {
        this.nearestStation = nearestStation;
    }

    public void setLimit(Integer  limit) {
        this.limit = limit;
    }

    public void setPage(Integer  page) {
        this.page = page;
    }
}

package com.cbt.cbtapr24eve;


import java.util.List;

public class OfferOrderView {

    String offerid;
    List<Order> orders;

    public String getOfferid() {
        return offerid;
    }

    public void setOfferid(String offerid) {
        this.offerid = offerid;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}

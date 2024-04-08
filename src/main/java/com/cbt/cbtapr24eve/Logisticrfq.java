package com.cbt.cbtapr24eve;

import jakarta.persistence.*;

@Entity
@Table(name = "logisticrfqs")
public class Logisticrfq {
    @Id
    @Column(name = "rfqid", nullable = false, length = 10)
    private String rfqid;

    @Column(name = "originport", nullable = false, length = 10)
    private String originport;

    @Column(name = "destinationport", nullable = false, length = 10)
    private String destinationport;

    @Column(name = "orderid", nullable = false, length = 10)
    private String orderid;

    @Column(name = "status", length = 10)
    private String status;

    public String getRfqid() {
        return rfqid;
    }

    public void setRfqid(String rfqid) {
        this.rfqid = rfqid;
    }

    public String getOriginport() {
        return originport;
    }

    public void setOriginport(String originport) {
        this.originport = originport;
    }

    public String getDestinationport() {
        return destinationport;
    }

    public void setDestinationport(String destinationport) {
        this.destinationport = destinationport;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
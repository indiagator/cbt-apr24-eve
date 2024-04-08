package com.cbt.cbtapr24eve;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "logisticrfqoffers")
public class Logisticrfqoffer {
    @Id
    @Column(name = "rfqofferid", nullable = false, length = 10)
    private String rfqofferid;

    @Column(name = "rfqid", length = 10)
    private String rfqid;

    @Column(name = "lpname", length = 50)
    private String lpname;

    @Column(name = "amnt")
    private Integer amnt;

    @Column(name = "status", length = 10)
    private String status;

    public String getRfqofferid() {
        return rfqofferid;
    }

    public void setRfqofferid(String rfqofferid) {
        this.rfqofferid = rfqofferid;
    }

    public String getRfqid() {
        return rfqid;
    }

    public void setRfqid(String rfqid) {
        this.rfqid = rfqid;
    }

    public String getLpname() {
        return lpname;
    }

    public void setLpname(String lpname) {
        this.lpname = lpname;
    }

    public Integer getAmnt() {
        return amnt;
    }

    public void setAmnt(Integer amnt) {
        this.amnt = amnt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
package com.cbt.cbtapr24eve;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usertypelinks")
public class Usertypelink {

    @Id
    @Column(name = "linkid", length = 500)
    private String linkid;

    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "type", length = 10)
    private String type;

    public String getLinkid() {
        return linkid;
    }

    public void setLinkid(String linkid) {
        this.linkid = linkid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
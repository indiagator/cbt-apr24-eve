package com.cbt.cbtapr24eve;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usernamewalletlinks")
public class Usernamewalletlink {

    @Id
    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "walletid", length = 500)
    private String walletid;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWalletid() {
        return walletid;
    }

    public void setWalletid(String walletid) {
        this.walletid = walletid;
    }

}
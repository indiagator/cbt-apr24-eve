package com.cbt.cbtapr24eve;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderstatusRepository extends JpaRepository<Orderstatus, String> {
    Orderstatus findByOrderid(String orderid);
}
package com.cbt.cbtapr24eve;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface OrderstatusRepository extends JpaRepository<Orderstatus, String> {

    Optional<Orderstatus> findByOrderid(String orderid);


    @Transactional
    @Modifying
    @Query("update Orderstatus o set o.status = ?1 where o.orderid = ?2")
    void updateStatusByOrderid(String accepted, String orderid);
}
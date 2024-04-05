package com.cbt.cbtapr24eve;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserdetailRepository extends JpaRepository<Userdetail, String> {

    Userdetail findByUsername(String username);

}
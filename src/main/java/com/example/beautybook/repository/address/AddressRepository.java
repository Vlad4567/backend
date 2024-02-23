package com.example.beautybook.repository.address;

import com.example.beautybook.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AddressRepository extends JpaRepository<Address,Long>,
        JpaSpecificationExecutor<Address> {

    @Query("SELECT DISTINCT a.city FROM Address a WHERE a.city LIKE %:city%")
    Page<String> findDistinctCity(@Param("city") String city, Pageable pageable);
}

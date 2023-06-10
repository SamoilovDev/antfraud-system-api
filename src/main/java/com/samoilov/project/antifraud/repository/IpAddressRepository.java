package com.samoilov.project.antifraud.repository;

import com.samoilov.project.antifraud.entity.IpAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IpAddressRepository extends JpaRepository<IpAddressEntity, Long> {

    @Query(value = "SELECT ip FROM IpAddressEntity ip WHERE ip.ip = :ip LIMIT 1")
    Optional<IpAddressEntity> findByIp(@Param("ip") String ip);

}

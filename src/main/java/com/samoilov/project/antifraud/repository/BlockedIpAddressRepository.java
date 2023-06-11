package com.samoilov.project.antifraud.repository;

import com.samoilov.project.antifraud.entity.IpAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockedIpAddressRepository extends JpaRepository<IpAddressEntity, Long> {

    Optional<IpAddressEntity> findByIp(String ip);

}

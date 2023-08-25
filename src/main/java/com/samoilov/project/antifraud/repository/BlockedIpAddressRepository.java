package com.samoilov.project.antifraud.repository;

import com.samoilov.project.antifraud.entity.IpAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockedIpAddressRepository extends JpaRepository<IpAddressEntity, Long> {

    Optional<IpAddressEntity> findByIp(String ip);

}

package com.samoilov.project.antifraud.repository;

import com.samoilov.project.antifraud.entity.BlockedIpAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockedIpAddressRepository extends JpaRepository<BlockedIpAddressEntity, Long> {

    Optional<BlockedIpAddressEntity> findByIp(String ip);

}

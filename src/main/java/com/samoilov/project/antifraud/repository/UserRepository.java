package com.samoilov.project.antifraud.repository;

import com.samoilov.project.antifraud.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM \"user\" WHERE \"username\" = ?1", nativeQuery = true)
    boolean deleteByUsername(String username);

    @Query(value = "SELECT * FROM \"user\"", nativeQuery = true, name = "getAll")
    List<UserEntity> getAll();

    @Query(value = "SELECT * FROM \"user\" WHERE \"username\" = ?1", nativeQuery = true)
    Optional<UserEntity> findByUsername(String username);

}

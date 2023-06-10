package com.samoilov.project.antifraud.repository;

import com.samoilov.project.antifraud.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Modifying
    @Query(value = "DELETE FROM UserEntity u WHERE u.username = :username")
    boolean deleteByUsername(@Param("username") String username);

    @Query(value = "SELECT u FROM UserEntity u ORDER BY u.id DESC")
    List<UserEntity> getAll();

    @Query(value = "SELECT u FROM UserEntity u WHERE u.username = :username ORDER BY u.id DESC LIMIT 1")
    Optional<UserEntity> findByUsername(@Param("username") String username);

}


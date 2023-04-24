package com.greengoldfish.repository;

import com.greengoldfish.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String login);

    @Query("SELECT u from User u JOIN FETCH u.authorities where u.email = :email")
    Optional<User> findByEmailFetchRoles(@Param("email") String email);
}

package it.cgmconsulting.msauth.repository;

import it.cgmconsulting.msauth.entity.User;
import it.cgmconsulting.msauth.payload.response.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsernameOrEmail(String username, String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByIdAndEnabledTrue(long id);

    @Query(value="SELECT u.username FROM User u WHERE u.id = :userId")
    String getUsername(@Param("userId") long userId);

    @Query(value="SELECT new it.cgmconsulting.msauth.payload.response.UserResponse(" +
            "u.id," +
            "u.username" +
            ") FROM User u " +
            "INNER JOIN u.authorities a " +
            "WHERE a.authorityName = :role")
    List<UserResponse> getUsersByRole(@Param("role") String role);
}

package com.ledgerlogic.repositories;

import com.ledgerlogic.models.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import lombok.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTests {

    @Autowired
    UserRepository userRepository;

    @Test
    void UserRepository_GetAll_ReturnMoreThanOneUser() {
        //Arrange
        User user = User.builder()
                .userId((long)151515)
                .username("jsmith0125")
                .firstName("John")
                .lastName("Smith")
                .email("JSmith@Gmail.com")
                .streetAddress("123 Main Street")
                .city("Atlanta")
                .state("GA")
                .zipCode("30518")
                .birthDay(Date.from(LocalDateTime.of(2024, 12, 25, 0, 0)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()))
                .status(true)
                .failedLoginAttempt((short)0).build();

        User user2 = User.builder()
                .userId((long)161616)
                .username("bwilson0125")
                .firstName("Brad")
                .lastName("Wilson")
                .email("BWilson@Gmail.com")
                .streetAddress("124 Main Street")
                .city("Atlanta")
                .state("GA")
                .zipCode("30518")
                .birthDay(Date.from(LocalDateTime.of(2024, 12, 25, 0, 0)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()))
                .status(true)
                .failedLoginAttempt((short)0).build();


        userRepository.save(user);
        userRepository.save(user2);

        //Act
        List<User> userList = userRepository.findAll();

        //Assert
        Assertions.assertThat(userList).isNotNull().hasSize(5);

    }

    @Test
    void UserRepository_FindByID_ReturnsOneUser() {
        User user = User.builder()
                .userId((long)151515)
                .username("jsmith0125")
                .firstName("John")
                .lastName("Smith")
                .email("JSmith@Gmail.com")
                .streetAddress("123 Main Street")
                .city("Atlanta")
                .state("GA")
                .zipCode("30518")
                .birthDay(Date.from(LocalDateTime.of(2024, 12, 25, 0, 0)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()))
                .status(true)
                .failedLoginAttempt((short)0).build();

        userRepository.save(user);

        User userReturn = userRepository.findById(user.getUserId()).get();

        Assertions.assertThat(userReturn).isNotNull();
    }
}

package com.example.parking.Service;

import com.example.parking.DTO.AdminDTO;
import com.example.parking.DTO.PasswordDTO;
import com.example.parking.DTO.UsernameDTO;
import com.example.parking.Model.MyUser;
import com.example.parking.Repository.MyUserRepository;
import com.example.parking.Service.MyUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
class MyUserServiceTest {

    @Autowired
    private MyUserRepository myUserRepository;

    private MyUserService myUserService;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @BeforeEach
    void setUp() {

        myUserService = new MyUserService(myUserRepository);
    }

//    @Test
//    void getUsers_ShouldReturnAllUsers() {
//        // Given
//        MyUser user1 = new MyUser(null, "user1", passwordEncoder.encode("password1"), "user1@example.com", "CUSTOMER", null, null);
//        MyUser user2 = new MyUser(null, "user2", passwordEncoder.encode("password2"), "user2@example.com", "ADMIN", null, null);
//        myUserRepository.save(user1);
//        myUserRepository.save(user2);
//
//        // When
//        List<MyUser> users = myUserService.getUsers();
//
//        // Then
//        assertEquals(2, users.size());
//        // Add more assertions as needed
//    }
//
//    @Test
//    void addAdmin_ShouldAddAdminUser() {
//        // Given
//        MyUser admin = new MyUser(null, "admin", "adminPassword", "admin@example.com", "ADMIN", null, null);
//        AdminDTO adminDTO = new AdminDTO(null, "Admin","adminPassword","admin@example.com");
//        // When
//        myUserService.addAdmin(adminDTO);
//
//        // Then
//        List<MyUser> users = myUserService.getUsers();
//        assertEquals(1, users.size());
//        // Add more assertions as needed
//    }

    @Test
    void updateUserPassword_ShouldUpdateUserPassword() {
        // Given
        MyUser user = new MyUser(null, "user", passwordEncoder.encode("oldPassword"), "user@example.com", "CUSTOMER", null, null);
        myUserRepository.save(user);
        PasswordDTO passwordDTO = new PasswordDTO("newPassword");

        // When
        myUserService.updateUserPassword(user, passwordDTO);

        // Then
        MyUser updatedUser = myUserRepository.findById(user.getId()).orElse(null);
        assertNotEquals(passwordEncoder.encode("newPassword"), updatedUser.getPassword());
        // Add more assertions as needed
    }

    @Test
    void updateUserUsername_ShouldUpdateUserUsername() {
        // Given
        MyUser user = new MyUser(null, "oldUsername", passwordEncoder.encode("password"), "user@example.com", "CUSTOMER", null, null);
        myUserRepository.save(user);
        UsernameDTO usernameDTO = new UsernameDTO("newUsername");

        // When
        myUserService.updateUserUsername(user, usernameDTO);

        // Then
        MyUser updatedUser = myUserRepository.findById(user.getId()).orElse(null);
        assertEquals("newUsername", updatedUser.getUsername());
        // Add more assertions as needed
    }
}


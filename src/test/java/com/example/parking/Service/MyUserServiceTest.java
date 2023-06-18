package com.example.parking.Service;

import com.example.parking.DTO.AdminDTO;
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

    @Test
    void getUsers_ShouldReturnAllUsers() {
        // Given
        MyUser user =myUserRepository.save(MyUser.builder().username("user1").password(passwordEncoder.encode("password1")).email("user1@example.com")
                .role( "CUSTOMER").build());
        MyUser admin =myUserRepository.save(MyUser.builder().username("admin").password(passwordEncoder.encode("admin1")).email("admin1@example.com")
                .role( "ADMIN").build());
        myUserRepository.save(user);
        myUserRepository.save(admin);

        // When
        List<MyUser> users = myUserService.getUsers();

        // Then
        assertEquals(7, users.size());
        // Add more assertions as needed
    }

    @Test
    void addAdmin_ShouldAddAdminUser() {
        // Given
        AdminDTO admin = new AdminDTO(1, "admin", "adminPassword99", "admin@example.com");

        myUserService.addAdmin(admin);

        // Then
        List<MyUser> users = myUserService.getUsers();
        assertEquals(6, users.size());
        // Add more assertions as needed
    }

    @Test
    void updateUserPassword_ShouldUpdateUserPassword() {
        // Given
        MyUser user =myUserRepository.save(MyUser.builder().username("user1").password(passwordEncoder.encode("password1")).email("user1@example.com")
                .role( "CUSTOMER").build());

        // When
        myUserService.updateUserPassword(user, "newPassword");

        // Then
        MyUser updatedUser = myUserRepository.findById(user.getId()).orElse(null);
        assertNotEquals(passwordEncoder.encode("newPassword"), updatedUser.getPassword());
        // Add more assertions as needed
    }

    @Test
    void updateUserUsername_ShouldUpdateUserUsername() {
        // Given
        MyUser savedUser=myUserRepository.save(MyUser.builder().username("user1").password(passwordEncoder.encode("password1")).email("user1@example.com")
                .role( "CUSTOMER").build());

        // When
        myUserService.updateUserUsername(savedUser, "newUsername");

        // Then
        MyUser updatedUser = myUserRepository.findById(savedUser.getId()).orElse(null);
        assertEquals("newUsername", updatedUser.getUsername());
        // Add more assertions as needed
    }
}


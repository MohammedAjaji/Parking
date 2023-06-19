package com.example.parking.Service;

import com.example.parking.DTO.AdminDTO;
import com.example.parking.DTO.CustomerDTO;
import com.example.parking.DTO.PasswordDTO;
import com.example.parking.DTO.UsernameDTO;
import com.example.parking.Model.MyUser;
import com.example.parking.Repository.MyUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyUserService {

    private final MyUserRepository myUserRepository;


    public List<MyUser> getUsers() {
       List<MyUser> users = myUserRepository.findAll();
        return users;
    }

    public void addAdmin(AdminDTO adminDTO) {
        String hash = new BCryptPasswordEncoder().encode(adminDTO.getPassword());
        MyUser user = new MyUser();

        user.setUsername(adminDTO.getUsername());
        user.setEmail(adminDTO.getEmail());
        user.setPassword(hash);
        user.setRole("ADMIN");
        myUserRepository.save(user);
    }

    public void updateUserPassword(MyUser myUser, PasswordDTO passwordDTO) {
        MyUser oldUser = myUserRepository.findMyUserById(myUser.getId());

        String hash = new BCryptPasswordEncoder().encode(passwordDTO.getPassword());
        oldUser.setPassword(hash);
        myUserRepository.save(oldUser);
    }

    public void updateUserUsername(MyUser myUser, UsernameDTO username) {
        MyUser oldUser = myUserRepository.findMyUserById(myUser.getId());

        oldUser.setUsername(username.getUsername());
        myUserRepository.save(oldUser);
    }
}

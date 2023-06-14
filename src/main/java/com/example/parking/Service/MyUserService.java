package com.example.parking.Service;

import com.example.parking.Model.MyUser;
import com.example.parking.Repository.MyUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyUserService {

    MyUserRepository myUserRepository;


    public List<MyUser> getUsers() {
        return myUserRepository.findAll();
    }

    public void addAdmin(MyUser user) {
        myUserRepository.save(user);
    }

    public void updateUserPassword(MyUser myUser, String password) {
        MyUser oldUser = myUserRepository.findMyUserById(myUser.getId());

        String hash = new BCryptPasswordEncoder().encode(password);
        oldUser.setPassword(hash);
        myUserRepository.save(oldUser);
    }

    public void updateUserUsername(MyUser myUser, String username) {
        MyUser oldUser = myUserRepository.findMyUserById(myUser.getId());

        oldUser.setUsername(username);
        myUserRepository.save(oldUser);
    }
}

package com.scm.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.UserRepo;
import com.scm.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public User saveUser(User user) {
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        return userRepo.save(user);
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
        User userById = userRepo.findById(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userById.setName(user.getName());
        userById.setEmail(user.getEmail());
        userById.setPassword(user.getPassword());
        userById.setAbout(user.getAbout());
        userById.setPhoneNumber(user.getPhoneNumber());
        userById.setProfilePic(user.getProfilePic());
        userById.setEnabled(user.isEnabled());
        userById.setEmailVerified(user.isEmailVerified());
        userById.setPhoneVerified(user.isPhoneVerified());
        userById.setProvider(user.getProvider());
        userById.setProviderUserId(user.getProviderUserId());

        User savedUser = userRepo.save(userById);

        return Optional.ofNullable(savedUser);
    }

    @Override
    public void deleteUser(String id) {
        User userById = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepo.delete(userById);
    }

    @Override
    public boolean isUserExist(String userId) {
        User userById = userRepo.findById(userId).orElse(null);
        return userById != null ? true : false;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User userByEmail = userRepo.findByEmail(email).orElse(null);
        return userByEmail != null ? true : false;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

}

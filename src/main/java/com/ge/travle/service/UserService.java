package com.ge.travle.service;

import com.ge.travle.domain.User;

public interface UserService {

    boolean register(User user);

    boolean active(String code);

    User login(User user);
}

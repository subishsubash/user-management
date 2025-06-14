package com.subash.user.management.service;

import com.subash.user.management.model.UserResponse;
import com.subash.user.management.model.UserView;
import org.springframework.http.ResponseEntity;

/**
 * User service interface
 */
public interface UserService {
    ResponseEntity<UserResponse> createUser(String uuid, UserView userView) throws Exception;

    ResponseEntity<UserResponse>  getUser(String uuid, String userName) throws Exception;
}

package com.subash.user.management.service;

import com.subash.user.management.model.UserResponse;
import com.subash.user.management.model.UserView;

/**
 * User service interface
 */
public interface UserService {
    UserResponse createUser(String uuid, UserView userView) throws Exception;
    UserResponse getUser(String uuid, UserView userView);
}

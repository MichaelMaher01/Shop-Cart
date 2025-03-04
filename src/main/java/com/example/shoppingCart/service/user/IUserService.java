package com.example.shoppingCart.service.user;

import com.example.shoppingCart.dto.UserDto;
import com.example.shoppingCart.model.User;
import com.example.shoppingCart.request.CreateUserRequest;
import com.example.shoppingCart.request.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request , Long userId);
     void deleteUser(Long userId);

    UserDto convertUserToDto(User user);

    User getAuthenticatedUser();
}

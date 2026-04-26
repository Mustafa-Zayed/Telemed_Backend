package com.mustafaz.telemed.users.service;

import com.mustafaz.telemed.res.Response;
import com.mustafaz.telemed.users.dto.UpdatePasswordRequest;
import com.mustafaz.telemed.users.dto.UserDTO;
import com.mustafaz.telemed.users.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    User getCurrentUser();

    Response<UserDTO> getMyUserDetails();

    Response<UserDTO> getUserById(Long userId);

    Response<List<UserDTO>> getAllUsers();

    Response<?> updatePassword(UpdatePasswordRequest updatePasswordRequest);

    Response<?> uploadProfilePicture(MultipartFile file);
}
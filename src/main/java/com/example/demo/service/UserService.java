package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.dto.RegisterRequestDTO;
import java.util.List;
import java.util.Set;

public interface UserService {
    UserDTO getUserInfo(Integer userId);
    List<UserDTO> getAllUsers();
    LoginResponseDTO login(LoginRequestDTO loginRequest);
    UserDTO register(RegisterRequestDTO registerRequest);
    UserDTO assignRolesToUser(Integer userId, Set<Integer> roleIds);
    UserDTO removeRolesFromUser(Integer userId, Set<Integer> roleIds);
    void deleteUser(Integer userId);
    UserDTO updateUser(Integer userId, UserDTO userDTO);
    List<UserDTO> getUsersByRoleName(String roleName);

} 
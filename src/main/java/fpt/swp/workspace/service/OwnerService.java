package fpt.swp.workspace.service;

import fpt.swp.workspace.DTO.UserDto;
import fpt.swp.workspace.models.User;
import fpt.swp.workspace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OwnerService {

    @Autowired
    private UserRepository userRepository;

//    public List<UserDto> getAllUsers() {
//        List<User> users = userRepository.findAll();
//        List<UserDto> userDtos = new ArrayList<>();
//        for (User user : users) {
//            UserDto userDto = new UserDto();
//            userDto.setUsername(user.getUsername());
//            userDto.setPassword(user.getPassword());
//            userDto.setRole(user.getRoleName());
//            userDtos.add(userDto);
//        }
//        return userDtos;
//    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.getAllUser();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = new UserDto();
            userDto.setUsername(user.getUsername());
            userDto.setPassword(user.getPassword());
            userDto.setRole(user.getRoleName());
            userDtos.add(userDto);
        }
        return userDtos;
    }

    public void deleteUser(String username) {
        User user = userRepository.findByuserName(username);
        user.setStatus("DISABLE");
        userRepository.save(user);
    }

}

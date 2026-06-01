package com.Rishikesh.UserService.service.imp;

import com.Rishikesh.UserService.exception.UserException;
import com.Rishikesh.UserService.modal.User;
import com.Rishikesh.UserService.repository.UserRepository;
import com.Rishikesh.UserService.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {


    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long Id) throws UserException {
        Optional<User> otp = userRepository.findById(Id);
        if(otp.isPresent()){
            return otp.get();
        }
        throw new UserException("User not found");
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long Id) throws UserException {
        Optional<User> otp = userRepository.findById(Id);
        if(otp.isEmpty()){
            throw new UserException("User not exist with ID"+ Id);
        }
        userRepository.deleteById(otp.get().getId());
    }

    @Override
    public User updateuser(Long id, User user) throws UserException {
        Optional<User> otp = userRepository.findById(id);
        if(otp.isEmpty()){
            throw new UserException("User not found with ID"+ id);
        }

        User existingUser = otp.get();
        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setRole(user.getRole());
        existingUser.setUsername(user.getUsername());

        return userRepository.save(existingUser);
    }
}

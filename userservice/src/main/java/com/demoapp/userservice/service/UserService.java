package com.demoapp.userservice.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.demoapp.userservice.dto.UserDto;
import com.demoapp.userservice.entity.UserEntity;
import com.demoapp.userservice.repository.UserRepository;

@Service
public class UserService {
	
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public UserDto saveUser(UserDto userDto) {
    	log.info("saving user into users table");
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        UserEntity savedUser = userRepository.save(userEntity);
        log.info("user into users table with Id: {}", savedUser);
        return modelMapper.map(savedUser, UserDto.class);
    }

    public Optional<UserDto> getUserById(Long id) {
    	log.info("finding user from users table for id: {}",id);
        return userRepository.findById(id)
                .map(user -> modelMapper.map(user, UserDto.class));
    }
}

package seyha.web.app.Bank_Concepts.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import seyha.web.app.Bank_Concepts.dto.UserDto;
import seyha.web.app.Bank_Concepts.entity.User;
import seyha.web.app.Bank_Concepts.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository  userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;


    public User registerUser(UserDto userResponse) {
        User user = UserMapToUserDTO(userResponse);
        return userRepository.save(user);
    }

    public Map<String, Object> authenticateUser(UserDto userDto) {
        Map<String, Object> authObject = new HashMap<String, Object>();
        User user = (User) userDetailsService.loadUserByUsername(userDto.getUsername());
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        authObject.put("token", "Bearer ".concat(jwtService.generateToken(userDto.getUsername())));
        authObject.put("user", user);
        return authObject;
    }

    private User UserMapToUserDTO(UserDto userResponse){

        User userRequest = new User();
        userRequest.setFirstname(userResponse.getFirstname());
        userRequest.setLastname(userResponse.getLastname());
        userRequest.setUsername(userResponse.getUsername());
        userRequest.setPassword(passwordEncoder.encode(userResponse.getPassword()));
        userRequest.setTag("cambank" + userResponse.getUsername());
        userRequest.setDob(userResponse.getDob());
        userRequest.setGender(userResponse.getGender());
        userRequest.setTel(userResponse.getTel());
        userRequest.setRoles(List.of("USER"));
        return userRepository.save(userRequest);
    }

}

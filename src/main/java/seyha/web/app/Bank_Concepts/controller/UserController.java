package seyha.web.app.Bank_Concepts.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seyha.web.app.Bank_Concepts.dto.UserDto;
import seyha.web.app.Bank_Concepts.entity.User;
import seyha.web.app.Bank_Concepts.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.registerUser(userDto));
    }

    public ResponseEntity<?> authenticationManager(@RequestBody UserDto userDto){
        var authObject = userService.authenticateUser(userDto);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, (String) authObject.get("token"))
                .body(authObject.get("user"));
    }

}

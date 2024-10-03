package seyha.web.app.Bank_Concepts.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String firstname;

    private String lastname;

    private String username;

    private Date dob;

    private long tel;

    private String tag;

    private String password;

    private String gender;
}

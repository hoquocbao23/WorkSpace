package fpt.swp.WorkSpace.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    private String userName;
    private String password;
    private String email;
    private String fullName;
    private Date dateOfBirth;
    private String phoneNumber;
    private String role;
}

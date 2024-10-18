package fpt.swp.workspace.auth;


import com.fasterxml.jackson.annotation.JsonInclude;
import fpt.swp.workspace.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse {

    private String status;
    private int statusCode;
    private String message;
    private User data;
    private String access_token;
    private String refresh_token;
    private String expired;






}

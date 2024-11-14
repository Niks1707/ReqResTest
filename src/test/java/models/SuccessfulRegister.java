package models;
import lombok.Data;

@Data
public class SuccessfulRegister {
    private int id;
    private String token;
    private String email;
    private String password;
}

package yhz.application.phase01.pojo;

import lombok.Data;

@Data
public class User {
    private int id;
    private String userName;
    private String userDescription;
    private String phone;
    private String password;
    private String email;
}

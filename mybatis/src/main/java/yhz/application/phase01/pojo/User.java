package yhz.application.phase01.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private int id;
    private String userName;
    private Date birthday;
    private String sex;
    private String address;
}

package yhz.demo.conf;

import lombok.Data;

@Data
public class DataSource {
    private String driver;
    private String url;
    private String userName;
    private String password;
    private String trsManagerType;
    private String dataSourceType;

}

package yhz.demo.conf;

import lombok.Data;
import java.util.Map;

@Data
public class Configration {
    private DataSource dataSource;

    public Configration() {
        this.dataSource = new DataSource();
    }
    private Map<String,MyMappedStatement> mapperSources;
}


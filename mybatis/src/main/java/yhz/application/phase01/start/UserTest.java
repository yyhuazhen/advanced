package yhz.application.phase01.start;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import yhz.application.phase01.pojo.User;
import yhz.application.phase01.pojo.dao.UserDao;
import yhz.application.phase01.pojo.dao.impl.UserDaoImpl;

import java.io.IOException;
import java.io.InputStream;

public class UserTest {
    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void init() {
        String resources = "phase01/SqlMapConfig.xml";
        try(InputStream inputStream = Resources.getResourceAsStream(resources);) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUserDao() {
        UserDao dao = new UserDaoImpl(sqlSessionFactory);
        User user = dao.findById(1);
        System.out.println(user.toString());
    }
}

package yhz.application.phase02.start;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import yhz.application.phase02.mapper.UserMapper;
import yhz.application.phase02.pojo.User;

import java.io.IOException;
import java.io.InputStream;

public class UserTest {
    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void init() {
        String resources = "phase02/SqlMapConfig.xml";
        try (InputStream inputStream = Resources.getResourceAsStream(resources);) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUserDao() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取mapper代理对象
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.findUserById(1);
        System.out.println(user.toString());
    }
}

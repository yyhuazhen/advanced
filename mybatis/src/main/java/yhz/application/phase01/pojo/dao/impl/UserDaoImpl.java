package yhz.application.phase01.pojo.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import yhz.application.phase01.pojo.User;
import yhz.application.phase01.pojo.dao.UserDao;

public class UserDaoImpl implements UserDao {
    private SqlSessionFactory sqlSessionFactory;
    public UserDaoImpl(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public User findById(int id) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = null;
        try{
            user = (User)sqlSession.selectOne("test.findById", id);
        }finally {
            sqlSession.close();
        }
        return user;
    }
}

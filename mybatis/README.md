备注：--> 方法层级调用关系 |--调用实例的类名  (类名)this：不是强转，表示的是this为该类名的实例
/*------------------------------------------mybatis主干-------------------------------------------------------*/

SqlSessionFactoryBuilder 
    -->build(InputStream inputStream):SqlSessionFactory
        -->build(InputStream inputStream, String environment, Properties properties):SqlSessionFactory 
            |--XMLConfigBuilder {XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment, properties)}
                -->parse():Configuration  【详情请看下面mybatis中xml解析流程】
            -->build(Configuration config):SqlSessionFactory {return new DefaultSqlSessionFactory(config)}
SqlSessionFactory
    -->openSession():SqlSession 【详情请看下面mybaitis中SqlSessionFactory获取SqlSession】
    |--DefaultSqlSession:SqlSession
        -->selectOne(String statement, Object parameter):Object
            -->selectList(String statement, Object parameter, RowBounds rowBounds):List {MappedStatement ms = (DefaultSqlSession)this.configuration.getMappedStatement(statement)；List var5 = (DefaultSqlSession)this.executor.query(ms, this.wrapCollection(parameter), rowBounds, Executor.NO_RESULT_HANDLER);}
          
**********************************************END(^_^)********************************************************* 



/*------------------------------------------mybatis中xml解析--------------------------------------------------*/
XMLConfigBuilder
    -->parse():Configration
        -->parseConfiguration(XNode root)
**********************************************END(^_^)*********************************************************




/*------------------------------------------mybaitis中SqlSessionFactory获取SqlSession-------------------------*/

**********************************************END(^_^)********************************************************* 

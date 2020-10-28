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
            -->selectList(String statement, Object parameter, RowBounds rowBounds):List 
            {MappedStatement ms = (DefaultSqlSession)this.configuration.getMappedStatement(statement)；
            List var5 = (DefaultSqlSession)this.executor.query(ms, this.wrapCollection(parameter), rowBounds, Executor.NO_RESULT_HANDLER);}
          
**********************************************END(^_^)********************************************************* 



/*------------------------------------------mybatis中xml解析--------------------------------------------------*/
XMLConfigBuilder
    -->parse():Configration
        -->parseConfiguration(XNode root) 【根据配置文件对有些没有设置的属性设置默认值，例如Configration中的ExcutorType 默认值：SIMPLE】
            |XMLMapperBuilder {new XMLMapperBuilder(inputStream, this.configuration, resource, this.configuration.getSqlFragments())}
                |--parse()
**********************************************END(^_^)*********************************************************




/*------------------------------------------mybaitis中SqlSessionFactory获取SqlSession-------------------------*/
DefaultSqlSessionFactory
    -->openSession():SqlSession
        -->openSessionFromDataSource(ExecutorType execType, TransactionIsolationLevel level, boolean autoCommit) :SqlSession 
        {1 获取Configration中的Enviroment对象 2根据Enviroment对象获取TransactionFactory 3根据TransactionFactory获取Transaction对象，注意Transaction对象中可以获取JDBC的连接Connection
        4根据Transaction对象和ExecutorType创建Executor对象 5 return new DefaultSqlSession(this.configuration, executor, autoCommit)} 
SqlSession
    -->excutor.操作() 【最终通过调用excutor中的方法进行数据库I/O】
**********************************************END(^_^)********************************************************* 

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
MappedStament:
SqlSource:
为什么在解析mapper中配置的mapper配置文件时要和当前线程绑定一个错误上下文？  --ErrorContext.instance().resource(resource):
**********************************************END(^_^)********************************************************* 



/*------------------------------------------mybatis中xml解析--------------------------------------------------*/
XMLConfigBuilder
    -->parse():Configration
        -->parseConfiguration(XNode root) 【根据配置文件对有些没有设置的属性设置默认值，例如Configration中的ExcutorType 默认值：SIMPLE】
            |XMLMapperBuilder {new XMLMapperBuilder(inputStream, this.configuration, resource, this.configuration.getSqlFragments())}
                |--parse()
**********************************************END(^_^)*********************************************************


/*---------------------------------------mybatis中mappers解析流程--------------------------------------------*/
XMLConfigBuilder
    -->mapperElement(XNode parent):void [mappers:Node]
		-->解析mappers中的子标签mapper
			-->解析mapper标签中可配置的属性，如:resource\url\class
				-->根据解析的属性加载相应的配置属性，resource和url不能同时设置，只能设置一个
					|--XMLMapperBuilder [new XMLMapperBuilder(inputStream, this.configuration, resource, this.configuration.getSqlFragments())]
						-->parse():void
						...
							-->buildStatementFromContext(context.evalNodes("select|insert|update|delete")):void [解析select、insert、update、delete标签]
								-->buildStatementFromContext(List<XNode> list, String requiredDatabaseId) 
									|--XMLStatementBuilder [new XMLStatementBuilder(this.configuration, this.builderAssistant, context(XNode), requiredDatabaseId)]
										-->parseStatementNode()：void {
											String nodeName = this.context.getNode().getNodeName();
											//根据nodeName判断是那种类型SqlCommandType，SqlCommandType有(select\insert\update\delete)
											SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));
											|--XMLIncludeTransformer {new XMLIncludeTransformer(this.configuration, this.builderAssistant)}  [主要是做什么工作?]
												-->applyIncludes(this.context.getNode())
													-->applyIncludes(Node source)
														-->获取Configration中的variables,并将variables中的值复制一份新的variables
															-->applyIncludes(Node source, Properties variablesContext, boolean included) [variablesContext就是上一步复制的Configration中的variables]
											|--LanguageDriver [主要是根据LanguageDriver创建SqlSource，默认的LanguageDriver是XmlLanguageDriver]	
												-->createSqlSource(Configuration configuration, XNode script, Class<?> parameterType):SqlSource
													|--XMLScriptBuilder {new XMLScriptBuilder(configuration, script, parameterType)}
														-->parseScriptNode():SqlSource
															-->parseDynamicTags(XNode node):MixedSqlNode
																|--TextSqlNode textSqlNode = new TextSqlNode(node.getBody());
																	-->textSqlNode.isDynamic():boolean  [根据返回值封装为动态SqlNode 还是静态SqlNode]
																		-->createParser(TextSqlNode.DynamicCheckerTokenParser checker = new TextSqlNode.DynamicCheckerTokenParser()):GenericTokenParser
																			|--GenericTokenParser
																				-->parse(node.getBody():String)
																					-->isDynamic():boolean
																	-->	return new MixedSqlNode(New ArrayList<SqlNode>(new StaticTextSqlNode(node.getBody())))
															-->	return new RawSqlSource(this.configuration, rootSqlNode:MixedSqlNode, this.parameterType) [此时会将含有#{}这种表达式的sql转成带?的动态sql]{
																-->static String getSql(Configuration configuration, SqlNode rootSqlNode) 
																	-->rootSqlNode.apply(new DynamicContext(configuration, (Object)null))[将MixedSqlNode中的SqlNode的text追加到StringBuilder，实际上由DynamicContext的实例对象完成]
																-->RawSqlSource(Configuration configuration, String sql, Class<?> parameterType) 
																	|--SqlSourceBuilder
																		-->parse(String originalSql, Class<?> parameterType, Map<String, Object> additionalParameters):SqlSOurce
															}
										}
SqlNode:
SqlSource:
**********************************************END(^_^)*********************************************************

/*------------------------------------------mybaitis中SqlSessionFactory获取SqlSession-------------------------*/
DefaultSqlSessionFactory
    -->openSession():SqlSession
        -->openSessionFromDataSource(ExecutorType execType, TransactionIsolationLevel level, boolean autoCommit) :SqlSession 
        {Environment environment = this.configuration.getEnvironment() [获取Configration中的Enviroment对象] 
		 TransactionFactory transactionFactory = this.getTransactionFactoryFromEnvironment(environment) [根据Enviroment对象获取TransactionFactory,如果enviroment对象中的TransactionFactory为null，则new ManagedTransactionFactory()，否则返回enviromnet对象中的事务工厂对象] 
		 transactionFactory.newTransaction(environment.getDataSource(), level, autoCommit) [根据TransactionFactory获取Transaction对象，注意Transaction对象中可以获取JDBC的连接Connection，其实是通过DataSource获取Connection]
         Executor executor = this.configuration.newExecutor(tx, execType) [根据Transaction对象和ExecutorType创建Executor对象]
		 return new DefaultSqlSession(this.configuration, executor, autoCommit) [返回SqlSession]
		 } 
SqlSession
    -->excutor.操作() 【最终通过调用excutor中的方法进行数据库I/O】
**********************************************END(^_^)********************************************************* 


/*------------------------------------------mybatis中结果集映射----------------------------------------------*/
根据SqlSession对数据库做增删改查：下面是查找流程
|--DefaultSqlSession
	-->selectOne(String statement, Object parameter):Object
		-->selectList(String statement, Object parameter):List
			-->selectList(String statement, Object parameter, RowBounds RowBounds.DEFAULT)
				-->MappedStatement ms = this.configuration.getMappedStatement(statement):MappedStament[获取MappedStatement对象，根据ID，因为有可能Confguration中有多个MappedStatement对象，所以需要根据id即:nameSpace的值+<select>标签中的id]
					-->wrapCollection(Object object):Object[解析参数，如果参数是集合或数组就解析成ParamMap，如果不是集合类或者数组就直接返回，不做处理]
						|--CachingExecutor [this.excutor.query调用]
							-->query(MappedStatement ms, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler)：List
								-->BoundSql boundSql = ms.getBoundSql(parameterObject) {
									|--MappedStament
										-->getBoundSql(Object parameterObject)
											|--RawSqlSource [调用this.sqlSource.getBoundSql]
												-->getBoundSql(Object parameterObject):BoundSql
													|--StaticSqlSource [调用this.sqlSource.getBoundSql]
														-->getBoundSql(Object parameterObject):BoundSql
															-->return new BoundSql(this.configuration, this.sql, this.parameterMappings, parameterObject);
								}
									-->CacheKey key = this.createCacheKey(ms, parameterObject, rowBounds, boundSql);[根据获取的MappedStament,参数等获取CacheKey]
										-->query(ms, parameterObject, rowBounds, resultHandler, key, boundSql)
											|--BaseExecutor[调用this.delegate.query]
												-->queryFromDatabase(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey key, BoundSql boundSql)：List
													|--SimpleExecutor
														-->doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql):List
															-->Configuration configuration = ms.getConfiguration();
																|--Configuration
																	-->configuration.newStatementHandler(this.wrapper, ms, parameter, rowBounds, resultHandler, boundSql):StatementHandler
																		-->StatementHandler statementHandler = new RoutingStatementHandler(executor, mappedStatement, parameterObject, rowBounds, resultHandler, boundSql)
																			|--InterceptorChain [通过this.interceptorChain.pluginAll(statementHandler)调用]
																				-->pluginAll(Object target)[InterceptorChain中封装了一个list用来存放Interceptor，该方法是将target与每个Interceptor绑定，调用的是Interceptor.plugin(Object target)中的 Plugin.wrap(target, this)来进行绑定，涉及到了动态反射，需要弄清楚为什么要这样做?（StatementHandler与InterceptorChain中的Interceptor绑定）]
																-->	prepareStatement(StatementHandler handler, Log statementLog)
																	-->Connection connection = this.getConnection(statementLog)[获取连接]
																		|--BaseStatementHandler
																			-->Statement stmt = handler.prepare(connection, this.transaction.getTimeout())[创建Statement类型的对象]
																				-->instantiateStatement(connection):Statement
																					-->return connection.prepareStatement(sql, this.mappedStatement.getResultSetType().getValue(), 1007)[要弄清楚参数为什么1007？]
																			-->parameterize(Statement statement)【动态参数的绑定】
																				|--PreparedStatementHandler[通过this.delegate.parameterize调用]
																					-->parameterize(Statement statement)
																						|--DefaultParameterHandler[调用方式：this.parameterHandler.setParameters((PreparedStatement)statement)]
																							-->setParameters(PreparedStatement ps)
																								-->this.boundSql.getParameterMappings()【获取boundsql中的ParametrMappings,根据ParameterMappings中的getProperty()方法获取传进去的参数值paramObject】
																									-->TypeHandler typeHandler = parameterMapping.getTypeHandler()【获取TypeHandler】
																										-->JdbcType jdbcType = parameterMapping.getJdbcType()【获取JdbcType】
																											|--BaseTypeHandler 【调用方式：typeHandler.setParameter(ps, i + 1, value, jdbcType)】
																												-->setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType)【i是从0开始，有多少个parameterMapping就有多个i，所以意思是parameterMapping的个数与?数相等】
																													-->setNonNullParameter(PreparedStatement ps, int i, Integer parameter, JdbcType jdbcType)
																														-->ps.setInt(i, parameter)【设置JDBC动态参数】
																	|--RoutingStatementHandler【调用方式：handler.query(stmt, resultHandler)】
																		|--PreparedStatementHandler【调用方式：this.delegate.query(statement, resultHandler)】	
																			-->query(Statement statement, ResultHandler resultHandler)
																				|--DefaultResultSetHandler【调用方式：this.resultSetHandler.handleResultSets(ps)】
																					-->handleResultSets(Statement stmt)
																						-->getFirstResultSet(Statement stmt):ResultSetWrapper 【封装查询结果集返回：new ResultSetWrapper(rs, this.configuration)，参数列表中rs是stmt.getResultSet获得的，在初始化ResultSetWrapper会获取元数据，通过rs.getResultgetMetaData()方法，之后会对元数据每个列的字段名，字段别名（通过()metadata.getColumnLabel()方法）,字段类型等放在configration中】
																							-->this.mappedStatement.getResultMaps()【获取配置文件中的结果映射】
																								-->handleResultSet(ResultSetWrapper rsw, ResultMap resultMap, List<Object> multipleResults, ResultMapping parentMapping)【处理结果集】
																									-->handleRowValues(ResultSetWrapper rsw, ResultMap resultMap, ResultHandler<?> resultHandler, RowBounds rowBounds, ResultMapping parentMapping)【处理结果集】
																										-->handleRowValuesForSimpleResultMap(ResultSetWrapper rsw, ResultMap resultMap, ResultHandler<?> resultHandler, RowBounds rowBounds, ResultMapping parentMapping)【处理结果集】
																											-->getRowValue(ResultSetWrapper rsw, ResultMap resultMap, String columnPrefix):Object【将查询结果封装在Object中】
																												-->applyAutomaticMappings(ResultSetWrapper rsw, ResultMap resultMap, MetaObject metaObject, String columnPrefix)【对上一步创建的Object参数值的设置】
																											-->storeObject(ResultHandler<?> resultHandler, DefaultResultContext<Object> resultContext, Object rowValue, ResultMapping parentMapping, ResultSet rs)【处理上一步封装的Object】
																												-->callResultHandler(ResultHandler<?> resultHandler, DefaultResultContext<Object> resultContext, Object rowValue)
																													|--DefaultResultContext【通过调用resultContext.nextResultObject(rowValue)】
																														-->nextResultObject(T resultObject)【将封装的Object的句柄给DefaultResultContext】
																															-->handleResult(ResultContext<?> context)【将封装的Context添加到DefaultResultHandler中的list中】
																																-->添加缓存
																																	-->返回结果
																											
相关接口:
Executor:执行接口，最终会调用StatementHandler接口																								
StatementHandler:最终执行查询语句接口，JDBC中由Statement接口执行操作
ResultHandler:处理结果集映射的接口
设计模式：这些Handler处理是不是用到了职责链模式？（更像是装饰模式？）
配置的结果映射和实际的怎么关联起来的？setMappings?addMappings()
**********************************************END(^_^)*********************************************************
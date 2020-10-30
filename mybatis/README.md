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




/*------------------------------------------mybaitis中SqlSessionFactory获取SqlSession-------------------------*/
DefaultSqlSessionFactory
    -->openSession():SqlSession
        -->openSessionFromDataSource(ExecutorType execType, TransactionIsolationLevel level, boolean autoCommit) :SqlSession 
        {1 获取Configration中的Enviroment对象 2根据Enviroment对象获取TransactionFactory 3根据TransactionFactory获取Transaction对象，注意Transaction对象中可以获取JDBC的连接Connection
        4根据Transaction对象和ExecutorType创建Executor对象 5 return new DefaultSqlSession(this.configuration, executor, autoCommit)} 
SqlSession
    -->excutor.操作() 【最终通过调用excutor中的方法进行数据库I/O】
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
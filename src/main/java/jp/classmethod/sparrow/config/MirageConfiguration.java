/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.classmethod.sparrow.config;

import javax.sql.DataSource;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mirage.repository.config.EnableMirageRepositories;
import org.springframework.data.mirage.repository.support.MiragePersistenceExceptionTranslator;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.slf4j.bridge.SLF4JBridgeHandler;

import jp.sf.amateras.mirage.SqlManagerImpl;
import jp.sf.amateras.mirage.bean.BeanDescFactory;
import jp.sf.amateras.mirage.bean.FieldPropertyExtractor;
import jp.sf.amateras.mirage.dialect.MySQLDialect;
import jp.sf.amateras.mirage.integration.spring.SpringConnectionProvider;
import jp.sf.amateras.mirage.naming.RailsLikeNameConverter;
import jp.sf.amateras.mirage.provider.ConnectionProvider;

import jp.classmethod.sparrow.SparrowApplication;

/**
 * Spring Data Mirage configuration.
 *
 * @author daisuke
 * @since #version#
 */
@RequiredArgsConstructor
@Configuration
@EnableTransactionManagement
@EnableMirageRepositories(basePackageClasses = SparrowApplication.class, sqlManagerRef = "sqlManager")
@SuppressWarnings("javadoc")
public class MirageConfiguration {
	
	private final DataSource dataSource;
	
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}
	
	@Bean
	public SqlManagerImpl sqlManager() {
		// bridge java.util.logging used by mirage
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		
		SqlManagerImpl sqlManagerImpl = new SqlManagerImpl();
		sqlManagerImpl.setConnectionProvider(connectionProvider());
		sqlManagerImpl.setDialect(new MySQLDialect());
		sqlManagerImpl.setBeanDescFactory(beanDescFactory());
		sqlManagerImpl.setNameConverter(new RailsLikeNameConverter());
		return sqlManagerImpl;
	}
	
	@Bean
	public ConnectionProvider connectionProvider() {
		SpringConnectionProvider springConnectionProvider = new SpringConnectionProvider();
		springConnectionProvider.setDataSource(dataSource);
		return springConnectionProvider;
	}
	
	@Bean
	public BeanDescFactory beanDescFactory() {
		BeanDescFactory beanDescFactory = new BeanDescFactory();
		beanDescFactory.setPropertyExtractor(new FieldPropertyExtractor());
		return beanDescFactory;
	}
	
	@Bean
	public MiragePersistenceExceptionTranslator persistenceExceptionTranslator() {
		return new MiragePersistenceExceptionTranslator();
	}
}

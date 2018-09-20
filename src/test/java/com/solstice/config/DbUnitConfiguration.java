//package com.solstice.config;
//
//import com.github.springtestdbunit.bean.DatabaseConfigBean;
//import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;
//import javax.persistence.PersistenceContext;
//import javax.persistence.PersistenceContextType;
//import javax.sql.DataSource;
//import org.dbunit.ext.mysql.MySqlDataTypeFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@PersistenceContext(type=PersistenceContextType.EXTENDED)
//public class DbUnitConfiguration {
//
//  @Autowired
//  private DataSource dataSource;
//
//  @Bean
//  public DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection() {
//    DatabaseConfigBean bean = new DatabaseConfigBean();
//    bean.setDatatypeFactory(new MySqlDataTypeFactory());
//
//    DatabaseDataSourceConnectionFactoryBean dbConnectionFactory = new DatabaseDataSourceConnectionFactoryBean(dataSource);
//    dbConnectionFactory.setDatabaseConfig(bean);
//    return dbConnectionFactory;
//  }
//}
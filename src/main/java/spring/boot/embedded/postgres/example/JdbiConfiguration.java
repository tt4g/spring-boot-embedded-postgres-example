package spring.boot.embedded.postgres.example;

import javax.sql.DataSource;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.spring4.SpringConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Setup jdbi3-spring4 (https://jdbi.org/#_spring) without beans.xml.
 *
 * This is instead of beans.xml that use org.jdbi.v3.spring4.JdbiFactoryBean.
 *
 * References: https://github.com/jdbi/jdbi/issues/1023#issuecomment-374108326
 *
 * org.springframework.jdbc.datasource.DataSourceTransactionManager provided by
 * spring-boot-starter-jdbc (see {@link EnableTransactionManagement}.
 *
 * NOTE: Don't use org.springframework.jdbc.datasource.DriverManagerDataSource that is too slow.
 */
@Configuration
public class JdbiConfiguration {

    @Bean
    @Autowired
    public Jdbi jdbi(DataSource dataSource) {
        // SpringConnectionFactory uses org.springframework.jdbc.datasource.DataSourceUtils
        // to get the connection from the DataSource.
        // This is the rule for getting the connection from the Spring managed
        // transaction manager.
        // NOTE: Can use org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy instead of SpringConnectionFactory.
        SpringConnectionFactory springConnectionFactory =
            new SpringConnectionFactory(dataSource);

        Jdbi jdbi = Jdbi.create(springConnectionFactory);
        jdbi.installPlugin(new PostgresPlugin());

        return jdbi;
    }

}

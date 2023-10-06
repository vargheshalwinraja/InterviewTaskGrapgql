package com.allwin.graphql;

import com.graphql.spring.boot.test.GraphQLTestTemplate;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = DemoGraphQlApplicationTests.Config.class)
@EnableAutoConfiguration
@EnableConfigurationProperties(MyProperties.class)
public class DemoGraphQlApplicationTests {
    @Autowired
    MyProperties properties;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    @LocalServerPort
    private int port;

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    // Configuration class for testing
    static class Config {}

    @Autowired
    private TestRestTemplate restTemplate;

    // Test to verify that the application context loads successfully
    @Test
    public void contextLoads() {
        assertNotNull(restTemplate);
        assertNotNull(jdbcTemplate);
        assertNotNull(dataSource);
    }

    // Test to verify the H2 database connection
    @Test
    @Order(1)
    public void h2Connection() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            assertNotNull(conn);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT 1");
            assertThat(rs).isNotNull();
        }
    }

    // Test to verify the H2 database version
    @Test
    @Order(2)
    public void h2Version() {
        String actualVersion = jdbcTemplate.queryForObject("SELECT `VALUE` FROM INFORMATION_SCHEMA.SETTINGS WHERE NAME = 'CREATE_BUILD'", String.class);
        String expectedVersion = properties.getH2().getVersion();
        assertThat(expectedVersion).endsWith("." + actualVersion); // Ensure the DB version matches the expected version
    }

    // Test to verify the 'artifactId' property
    @Test
    @Order(3)
    public void artifactProperty() throws IOException {
        assertThat(properties.getArtifactId()).isEqualTo("graphql");
    }
}

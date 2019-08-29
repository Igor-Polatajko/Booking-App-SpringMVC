package app.daos;

import app.configurations.TestConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebAppConfiguration
@ContextConfiguration(classes = {TestConfig.class})
class BaseDaoTest {

    @Autowired
    protected JdbcTemplate template;

    void clearTables(String... tableNames) {
        final String[] cleanupQueries = new String[tableNames.length + 2];
        cleanupQueries[0] = "SET foreign_key_checks=0";
        cleanupQueries[cleanupQueries.length - 1] = "SET foreign_key_checks=1";
        for (int index = 0; index < tableNames.length; index++) {
            cleanupQueries[index + 1] = "TRUNCATE TABLE " + tableNames[index];
        }
        template.batchUpdate(cleanupQueries);
    }

}

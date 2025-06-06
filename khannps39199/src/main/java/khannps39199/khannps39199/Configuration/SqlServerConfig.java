package khannps39199.khannps39199.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

@Configuration
public class SqlServerConfig {
	@Bean
	public SQLServerDataSource sqlServerDataSource() {
		return new SQLServerDataSource();
	}

	@Bean
	public JdbcTemplate jdbcTemplate(SQLServerDataSource ds) {
		return new JdbcTemplate(ds);
	}

}

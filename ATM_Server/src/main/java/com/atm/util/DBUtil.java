package com.atm.util;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public class DBUtil {

	private static EmbeddedDatabase getDataSource() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		
		
		EmbeddedDatabase db = builder.setType(EmbeddedDatabaseType.DERBY).build();
		builder.continueOnError(true);
		builder.addScript("schema.sql").addScript("data.sql");
		return db;
	}

	@Bean
	public static JdbcTemplate getJdbcTemplate() {
		return new JdbcTemplate(getDataSource());
	}

}

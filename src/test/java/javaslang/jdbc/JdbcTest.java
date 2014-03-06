package javaslang.jdbc;

import java.util.Optional;

import javaslang.util.Try;

import javax.sql.DataSource;

// @Singleton
public class JdbcTest {

	// @Injected
	final DataSource ds = null;

	final Jdbc jdbc = new Jdbc(() -> ds.getConnection());

	Try<Optional<Person>> getPersonByName(String name) {
		return Try.of(() -> jdbc.query("select * from Person where name = :name")
				.withParams((stmt, $) -> {
					stmt.setString("name", name);
				})
				.optional((rs, index) -> {
					return new Person(rs.getLong("id"), rs.getString("name"));
				}));
	}
	
	void client() {
		
		JdbcTest service = new JdbcTest(); // Context.getBean(JdbcTest.class);
		
		final Try<Optional<Person>> person = service.getPersonByName("Hans");

	}
	
	static class Person {
		final Long id;
		final String name;

		Person(Long id, String name) {
			this.id = id;
			this.name = name;
		}
	}

}

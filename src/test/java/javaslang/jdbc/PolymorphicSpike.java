package javaslang.jdbc;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class PolymorphicSpike {
	
	PreparedStatement ps;

	void on(String name, byte x) {}
	void on(String name, short x) {}
	void on(String name, int x) {}
	void on(String name, long x) {}
	void on(String name, float x) {}
	void on(String name, double x) {}
	void on(String name, char x) {}
	void on(String name, Integer x) {}
	
	void on(String name, String x) throws SQLException {
		on(name, x, ps::setString, Types.CHAR); // TODO: CHAR(n) vs VRACHAR(n)
	}
	
	void on(String name, BigDecimal x) throws SQLException {
		on(name, x, ps::setBigDecimal, Types.DECIMAL); // TODO: REAL vs. FLOAT vs. DECIMAL
	}
	
	<T> void on(String name, T value, PreparedStatementSetter<T> setter, int sqlType) throws SQLException {
		final int parameterIndex = 0; // TODO: indizes.get(name)
		if (value == null) {
			ps.setNull(parameterIndex, sqlType);
		} else {
			setter.set(parameterIndex, value);
		}
	}
	
	@FunctionalInterface
	static interface PreparedStatementSetter<T> {
		void set(int parameterIndex, T value) throws SQLException;
	}
	
	void test() throws SQLException {
		on("name", "Hello");
		on("count", 1);
		on("name", (String) null);
	}
	
}

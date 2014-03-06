package javaslang.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapper<T> {
	
	// TODO: add SaveResultSet which checks null etc.
	T mapRow(ResultSet rs, int index) throws SQLException;

}

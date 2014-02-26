package javaslang.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Executable {

	<T> List<T> list(RowMapper<T> rowMapper) throws SQLException;
	<T> T single(RowMapper<T> rowMapper) throws SQLException;
	<T> Optional<T> optional(RowMapper<T> rowMapper) throws SQLException;

}

package javaslang.jdbc;

import static java.util.Arrays.asList;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javaslang.util.function.CheckedSupplier;

public final class Query implements Executable {
	
	private final ConnectionSupplier connectionSupplier;
	private final String sql;
	
	Query(ConnectionSupplier connectionSupplier, String sql) {
		this.connectionSupplier = connectionSupplier;
		this.sql = sql;
	}
	
	@SafeVarargs
	public final <P> ParameterizedQuery<P> withParams(ParamSetter<P> paramSetter, P... params) {
		return withParams(paramSetter, asList(params));
	}

	public <P> ParameterizedQuery<P> withParams(ParamSetter<P> paramSetter, List<P> params) {
		return new ParameterizedQuery<P>(connectionSupplier, sql, paramSetter, params);
	}

	@Override
	public <T> List<T> list(RowMapper<T> rowMapper) throws SQLException {
		return withParams(null).list(rowMapper);
	}

	@Override
	public <T> T single(RowMapper<T> rowMapper) throws SQLException {
		return withParams(null).single(rowMapper);
	}

	@Override
	public <T> Optional<T> optional(RowMapper<T> rowMapper) throws SQLException {
		return withParams(null).optional(rowMapper);
	}

}

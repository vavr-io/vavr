package javaslang.jdbc;

import static javaslang.lang.Lang.require;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParameterizedQuery<P> implements Executable {
	
	private final ConnectionSupplier connectionSupplier;
	private final String sql;
	private final ParamSetter<P> paramSetter;
	private final List<P> params;
	
	ParameterizedQuery(ConnectionSupplier connectionSupplier, String sql, ParamSetter<P> paramSetter, List<P> params) {
		require(connectionSupplier != null, "connection supplier is null");
		require(sql != null, "sql is null");
		require(params != null, "parameter list is null");
		this.connectionSupplier = connectionSupplier;
		this.sql = sql;
		this.paramSetter = paramSetter;
		this.params = params;
	}
	
	@Override
	public <T> List<T> list(RowMapper<T> rowMapper) throws SQLException {
		return select(resultSet -> {
			final List<T> result = new ArrayList<T>();
			int index = 0;
			while (resultSet.next()) {
				final T bean = rowMapper.mapRow(resultSet, index++);
				result.add(bean);
			}
			return result;
		});
	}

	@Override
	public <T> T single(RowMapper<T> rowMapper) throws SQLException {
		return select(resultSet -> {
			if (!resultSet.next()) {
				throw new SQLException("No result.");
			}
			final T bean = rowMapper.mapRow(resultSet, 0);
			if (resultSet.next()) {
				throw new SQLException("More than one result.");
			}
			return bean;
		});
	}

	@Override
	public <T> Optional<T> optional(RowMapper<T> rowMapper) throws SQLException {
		return select(resultSet -> {
			if (!resultSet.next()) {
				return Optional.empty();
			}
			final T bean = rowMapper.mapRow(resultSet, 0);
			if (resultSet.next()) {
				throw new SQLException("More than one result.");
			}
			return Optional.of(bean);
		});
	}
	
	private <T,R> R select(ResultSetProcessor<T,R> processor) throws SQLException {
		require(params.size() <= 1, "A select query cannot be parameterized more than one time.");
		try (final Connection connection = connectionSupplier.get()) {
			try (final NamedParameterStatement statement = new NamedParameterStatement(connection, sql)) {
				if (paramSetter != null && params.size() > 0) {
					paramSetter.setParams(statement, params.get(0));
				}
				try (ResultSet resultSet = statement.executeQuery()) {
					return processor.process(resultSet);
				}
			}
		}
	}
	
	@FunctionalInterface
	private static interface ResultSetProcessor<T,R> {
		R process(ResultSet resultSet) throws SQLException;
	}
	
}

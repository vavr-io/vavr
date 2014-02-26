package javaslang.jdbc;

import java.sql.SQLException;

@FunctionalInterface
public interface ParamSetter<P> {
	
	void setParams(NamedParameterStatement statement, P params) throws SQLException;
	
}

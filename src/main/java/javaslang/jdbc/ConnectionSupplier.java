package javaslang.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface ConnectionSupplier {

	Connection get() throws SQLException;

}

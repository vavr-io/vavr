package javaslang.jdbc;

import java.sql.Connection;
import java.util.function.Supplier;

public class Jdbc {
	
	private final Supplier<Connection> connectionSupplier;

	public Jdbc(Supplier<Connection> connectionSupplier) {
		this.connectionSupplier = connectionSupplier;
	}
	
	public Query query(String sql) {
		return new Query(connectionSupplier, sql);
	}

}

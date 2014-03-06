package javaslang.jdbc;


public class Jdbc {
	
	private final ConnectionSupplier connectionSupplier;

	public Jdbc(ConnectionSupplier connectionSupplier) {
		this.connectionSupplier = connectionSupplier;
	}
	
	public Query query(String sql) {
		return new Query(connectionSupplier, sql);
	}

}

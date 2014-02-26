package javaslang.jdbc;

import javaslang.jdbc.Jdbc;

import java.sql.SQLException;
import java.util.Optional;

public class DAOSpike {

	// @Inject
	Jdbc jdbc = null;

	public void create(Trade trade) throws SQLException {
		
		
//		jdbc.update("insert into table set name=?, count=?", trade, (ps, params) -> {
//			int index = 1;
//			ps.setString(index++, params.getName());
//			ps.setInt(index++, params.getCount());
//		});
//		
//		jdbc.list("select 1+1 from dual", (rs, index) -> {
//			return null;
//		});
	}
	
	public void save(Trade trade) throws SQLException {
		// TODO
	}
	
	public void delete(Trade trade) throws SQLException {
		// TODO
	}
	
	public Optional<Trade> findById() throws SQLException {
		// TODO
		return null;
	}
	
	public Trade getById() throws SQLException {
		// TODO
		return null;
	}
	
	static class Trade {
		String getName() { return null; }
		int getCount() { return -1; }
	}
	
}

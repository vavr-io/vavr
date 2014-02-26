package javaslang.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NamedParameterStatement implements AutoCloseable {

	private final static Parser PARSER = new Parser();
	
	private final PreparedStatement statement;
	private final Map<String, List<Integer>> indices;

	NamedParameterStatement(Connection connection, String sql) throws SQLException {
		final Parser.ParseResult parseResult = PARSER.parse(sql);
		final String substitutedSql = parseResult.getSubstitutedSql();
		this.statement = connection.prepareStatement(substitutedSql);
		this.indices = parseResult.getIndices();
	}

	ResultSet executeQuery() throws SQLException {
		return statement.executeQuery();
	}

	/** Replacement for {@link PreparedStatement#setArray(int, Array)}. */
	public void setArray(String name, Array x) throws SQLException {
		getIndices(name).forEach(index -> statement.setArray(index, x));
	}

	/** Replacement for {@link PreparedStatement#setAsciiStream(int, InputStream)}. */
	public void setAsciiStream(String name, InputStream x) throws SQLException {
		getIndices(name).forEach(index -> statement.setAsciiStream(index, x));
	}

	/** Replacement for {@link PreparedStatement#setAsciiStream(int, InputStream, int)}. */
	public void setAsciiStream(String name, InputStream x, int length) throws SQLException {
		getIndices(name).forEach(index -> statement.setAsciiStream(index, x, length));
	}

	/** Replacement for {@link PreparedStatement#setAsciiStream(int, InputStream, long)}. */
	public void setAsciiStream(String name, InputStream x, long length) throws SQLException {
		getIndices(name).forEach(index -> statement.setAsciiStream(index, x, length));
	}

	/** Replacement for {@link PreparedStatement#setBigDecimal(int, BigDecimal)}. */
	public void setBigDecimal(String name, BigDecimal x) throws SQLException {
		getIndices(name).forEach(index -> statement.setBigDecimal(index, x));
	}

	/** Replacement for {@link PreparedStatement#setBinaryStream(int, InputStream)}. */
	public void setBinaryStream(String name, InputStream x) throws SQLException {
		getIndices(name).forEach(index -> statement.setBinaryStream(index, x));
	}

	/** Replacement for {@link PreparedStatement#setBinaryStream(int, InputStream, int))}. */
	public void setBinaryStream(String name, InputStream x, int length) throws SQLException {
		getIndices(name).forEach(index -> statement.setBinaryStream(index, x, length));
	}

	/** Replacement for {@link PreparedStatement#setBinaryStream(int, InputStream, long)}. */
	public void setBinaryStream(String name, InputStream x, long length) throws SQLException {
		getIndices(name).forEach(index -> statement.setBinaryStream(index, x, length));
	}

	/** Replacement for {@link PreparedStatement#setBlob(int, Blob)}. */
	public void setBlob(String name, Blob x) throws SQLException {
		getIndices(name).forEach(index -> statement.setBlob(index, x));
	}

	/** Replacement for {@link PreparedStatement#setBlob(int, InputStream)}. */
	public void setBlob(String name, InputStream inputStream) throws SQLException {
		getIndices(name).forEach(index -> statement.setBlob(index, inputStream));
	}

	/** Replacement for {@link PreparedStatement#setBlob(int, InputStream, long)}. */
	public void setBlob(String name, InputStream inputStream, long length) throws SQLException {
		getIndices(name).forEach(index -> statement.setBlob(index, inputStream, length));
	}

	/** Replacement for {@link PreparedStatement#setBoolean(int, boolean)}. */
	public void setBoolean(String name, boolean x) throws SQLException {
		getIndices(name).forEach(index -> statement.setBoolean(index, x));
	}

	/** Replacement for {@link PreparedStatement#setByte(int, byte)}. */
	public void setByte(String name, byte x) throws SQLException {
		getIndices(name).forEach(index -> statement.setByte(index, x));
	}

	/** Replacement for {@link PreparedStatement#setBytes(int, byte[])}. */
	public void setBytes(String name, byte[] x) throws SQLException {
		getIndices(name).forEach(index -> statement.setBytes(index, x));
	}

	/** Replacement for {@link PreparedStatement#setCharacterStream(int, Reader)}. */
	public void setCharacterStream(String name, Reader reader) throws SQLException {
		getIndices(name).forEach(index -> statement.setCharacterStream(index, reader));
	}

	/** Replacement for {@link PreparedStatement#setCharacterStream(int, Reader, int)}. */
	public void setCharacterStream(String name, Reader reader, int length) throws SQLException {
		getIndices(name).forEach(index -> statement.setCharacterStream(index, reader, length));
	}

	/** Replacement for {@link PreparedStatement#setCharacterStream(int, Reader, long)}. */
	public void setCharacterStream(String name, Reader reader, long length) throws SQLException {
		getIndices(name).forEach(index -> statement.setCharacterStream(index, reader, length));
	}

	/** Replacement for {@link PreparedStatement#setClob(int, Clob)}. */
	public void setClob(String name, Clob x) throws SQLException {
		getIndices(name).forEach(index -> statement.setClob(index, x));
	}

	/** Replacement for {@link PreparedStatement#setClob(int, Reader)}. */
	public void setClob(String name, Reader reader) throws SQLException {
		getIndices(name).forEach(index -> statement.setClob(index, reader));
	}

	/** Replacement for {@link PreparedStatement#setNClob(int, Reader, long)}. */
	public void setClob(String name, Reader reader, long length) throws SQLException {
		getIndices(name).forEach(index -> statement.setNClob(index, reader, length));
	}

	/** Replacement for {@link PreparedStatement#setDate(int, Date)}. */
	public void setDate(String name, Date x) throws SQLException {
		getIndices(name).forEach(index -> statement.setDate(index, x));
	}

	/** Replacement for {@link PreparedStatement#setDate(int, Date, Calendar)}. */
	public void setDate(String name, Date x, Calendar cal) throws SQLException {
		getIndices(name).forEach(index -> statement.setDate(index, x, cal));
	}

	/** Replacement for {@link PreparedStatement#setDouble(int, double)}. */
	public void setDouble(String name, double x) throws SQLException {
		getIndices(name).forEach(index -> statement.setDouble(index, x));
	}

	/** Replacement for {@link PreparedStatement#setFloat(int, float)}. */
	public void setFloat(String name, float x) throws SQLException {
		getIndices(name).forEach(index -> statement.setFloat(index, x));
	}

	/** Replacement for {@link PreparedStatement#setInt(int, int)}. */
	public void setInt(String name, int x) throws SQLException {
		getIndices(name).forEach(index -> statement.setInt(index, x));
	}

	/** Replacement for {@link PreparedStatement#setLong(int, long)}. */
	public void setLong(String name, long x) throws SQLException {
		getIndices(name).forEach(index -> statement.setLong(index, x));
	}

	/** Replacement for {@link PreparedStatement#setNCharacterStream(int, Reader)}. */
	public void setNCharacterStream(String name, Reader value) throws SQLException {
		getIndices(name).forEach(index -> statement.setNCharacterStream(index, value));
	}

	/** Replacement for {@link PreparedStatement#setNCharacterStream(int, Reader, long)}. */
	public void setNCharacterStream(String name, Reader value, long length) throws SQLException {
		getIndices(name).forEach(index -> statement.setNCharacterStream(index, value, length));
	}

	/** Replacement for {@link PreparedStatement#setNClob(int, NClob)}. */
	public void setNClob(String name, NClob value) throws SQLException {
		getIndices(name).forEach(index -> statement.setNClob(index, value));
	}

	/** Replacement for {@link PreparedStatement#setNClob(int, Reader)}. */
	public void setNClob(String name, Reader reader) throws SQLException {
		getIndices(name).forEach(index -> statement.setNClob(index, reader));
	}

	/** Replacement for {@link PreparedStatement#}. */
	public void setNClob(String name, Reader reader, long length) throws SQLException {
		getIndices(name).forEach(index -> statement.setNClob(index, reader, length));
	}

	/** Replacement for {@link PreparedStatement#setNString(int, String)}. */
	public void setNString(String name, String value) throws SQLException {
		getIndices(name).forEach(index -> statement.setNString(index, value));
	}

	/** Replacement for {@link PreparedStatement#setNull(int, int)}. */
	public void setNull(String name, int sqlType) throws SQLException {
		getIndices(name).forEach(index -> statement.setNull(index, sqlType));
	}

	/** Replacement for {@link PreparedStatement#setNull(int, int, String)}. */
	public void setNull(String name, int sqlType, String typeName) throws SQLException {
		getIndices(name).forEach(index -> statement.setNull(index, sqlType, typeName));
	}

	/** Replacement for {@link PreparedStatement#setObject(int, Object)}. */
	public void setObject(String name, Object x) throws SQLException {
		getIndices(name).forEach(index -> statement.setObject(index, x));
	}

	/** Replacement for {@link PreparedStatement#setObject(index, x, targetSqlType))}. */
	public void setObject(String name, Object x, int targetSqlType) throws SQLException {
		getIndices(name).forEach(index -> statement.setObject(index, x, targetSqlType));
	}

	/** Replacement for {@link PreparedStatement#setObject(int, Object, java.sql.SQLType, int)}. */
	public void setObject(String name, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
		getIndices(name).forEach(index -> statement.setObject(index, x, targetSqlType, scaleOrLength));
	}

	/** Replacement for {@link PreparedStatement#setRef(int, Ref)}. */
	public void setRef(String name, Ref x) throws SQLException {
		getIndices(name).forEach(index -> statement.setRef(index, x));
	}

	/** Replacement for {@link PreparedStatement#setRowId(int, RowId)}. */
	public void setRowId(String name, RowId x) throws SQLException {
		getIndices(name).forEach(index -> statement.setRowId(index, x));
	}

	/** Replacement for {@link PreparedStatement#setShort(int, short)}. */
	public void setShort(String name, short x) throws SQLException {
		getIndices(name).forEach(index -> statement.setShort(index, x));
	}

	/** Replacement for {@link PreparedStatement#setSQLXML(int, SQLXML)}. */
	public void setSQLXML(String name, SQLXML xmlObject) throws SQLException {
		getIndices(name).forEach(index -> statement.setSQLXML(index, xmlObject));
	}

	/** Replacement for {@link PreparedStatement#setString(int, String)}. */
	public void setString(String name, String x) throws SQLException {
		getIndices(name).forEach(index -> statement.setString(index, x));
	}

	/** Replacement for {@link PreparedStatement#setTime(int, Time)}. */
	public void setTime(String name, Time x) throws SQLException {
		getIndices(name).forEach(index -> statement.setTime(index, x));
	}

	/** Replacement for {@link PreparedStatement#setTime(int, Time, Calendar)}. */
	public void setTime(String name, Time x, Calendar cal) throws SQLException {
		getIndices(name).forEach(index -> statement.setTime(index, x, cal));
	}

	/** Replacement for {@link PreparedStatement#setTimestamp(int, Timestamp)}. */
	public void setTimestamp(String name, Timestamp x) throws SQLException {
		getIndices(name).forEach(index -> statement.setTimestamp(index, x));
	}

	/** Replacement for {@link PreparedStatement#setTimestamp(int, Timestamp, Calendar)}. */
	public void setTimestamp(String name, Timestamp x, Calendar cal) throws SQLException {
		getIndices(name).forEach(index -> statement.setTimestamp(index, x, cal));
	}

	/** Replacement for {@link PreparedStatement#setURL(int, URL)}. */
	public void setURL(String name, URL x) throws SQLException {
		getIndices(name).forEach(index -> statement.setURL(index, x));
	}

	private Indices getIndices(String name) {
		final List<Integer> _indices = indices.get(name);
		return new Indices(_indices);
	}

	private static class Indices {
		final List<Integer> indices;

		Indices(List<Integer> indices) {
			this.indices = indices;
		}

		void forEach(IndexConsumer consumer) throws SQLException {
			for (int index : indices) {
				consumer.consume(index);
			}
		}

		@FunctionalInterface
		static interface IndexConsumer {
			void consume(int index) throws SQLException;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws SQLException {
		if (statement != null) {
			statement.close();
		}
	}

	static class Parser {

//		final Lexer lexer;
//
//		Parser() {
//			final Pattern paramName = Pattern.compile("[\\p{L}_$][\\p{L}\\p{N}_$]*");
//			final Tree<Element> grammar = new GrammarBuilder() //
//					.comment("--") // single-line comment
//					.comment("/*", "*/") // multi-line comment
//					.string("'", "'").withEscape("'") // string
//					.string("\"", "\"") // quoted sql identifier
//					.identifier(":", paramName) // named parameter
//					.identifier(":{", "}") // named parameter
//					.identifier("?") // jdbc prepared statement placeholder
//					.skip(asList("::", "\\:")) // postgres casting operator, escaped colon
//					.build();
//			lexer = new Lexer("SqlNamedParameterLexer", grammar);
//		}

		ParseResult parse(String sql) {
//			final Tree<Token> ast = lexer.tokenize(sql);
//			return new ParseResult(ast);
			return null; // TODO
		}

		static class ParseResult {

//			final Tree<Token> ast;
//
//			ParseResult(Tree<Token> ast) {
//				this.ast = ast;
//			}

			// TODO
			Map<String, List<Integer>> getIndices() {
				final Map<String, List<Integer>> result = new HashMap<>();
//				final AtomicInteger _int = new AtomicInteger();
//				ast.traverse(tree -> {
//					if ("Identifier".equals(tree.id)) {
//						final int childIndex = tree.children().size() == 1 ? 0 : 1;
//						final String name = tree.children().get(childIndex).value().text;
//						final int i = _int.incrementAndGet();
//						if (!"?".equals(name)) {
//							final List<Integer> indices = result.containsKey(name) ? result.get(name)
//									: new ArrayList<>();
//							result.put(name, indices);
//							indices.add(i);
//						}
//						return false;
//					}
//					return true;
//				});
				return result;
			}
			
			// TODO
			String getSubstitutedSql() {
				final StringBuffer sql = new StringBuffer();
//				ast.traverse(tree -> {
//					if ("Identifier".equals(tree.id)) {
//						sql.append("?");
//						return false;
//					} else if (tree.isLeaf()) {
//						sql.append(tree.value().text);
//					}
//					return true;
//				});
				return sql.toString();
			}
		}
	}
}

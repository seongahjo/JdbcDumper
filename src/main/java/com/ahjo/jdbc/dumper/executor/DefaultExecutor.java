package com.ahjo.jdbc.dumper.executor;

import com.ahjo.jdbc.dumper.Row;
import com.ahjo.jdbc.dumper.TableMetadata;
import com.ahjo.jdbc.dumper.generator.Generator;
import com.ahjo.jdbc.dumper.parser.Parser;
import com.ahjo.jdbc.dumper.sql.SelectSql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DefaultExecutor implements Executor {
	protected Generator generator;
	protected Parser parser;
	protected final Connection connection;

	public DefaultExecutor(String driverClassName, String url, String userName, String password,
						   Generator generator, Parser parser) {
		this.generator = generator;
		this.parser = parser;
		this.connection = this.connect(driverClassName, url, userName, password);
	}

	protected <T> T executeMetadata(ExecutorCallback<DatabaseMetaData, T> callback) {
		T value = null;
		try {
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			value = callback.execute(databaseMetaData);
		} catch (SQLException e) {

		}
		return value;
	}

	protected <T> T execute(ExecutorCallback<Statement, T> callback) {
		T value = null;
		try (Statement statement = connection.createStatement()) {
			value = callback.execute(statement);
		} catch (SQLException e) {

		}
		return value;
	}

	@Override
	public List<Row> query(String databaseName, SelectSql sql) {
		return execute(statement -> parser.parseRow(statement.executeQuery(sql.toString())));
	}

	@Override
	public List<String> showTables(String databaseName) {
		return executeMetadata(dbMetaData ->
				parser.parseTableNames(dbMetaData.getTables(null, null, null, new String[]{"TABLE"})));
	}

	@Override
	public TableMetadata inspectTable(String databaseName, String tableName) {
		return executeMetadata(dbMetaData -> {
			TableMetadata tableMetadata = new TableMetadata(tableName);
			tableMetadata.addColumnInfos(parser.parseColumnInfo(dbMetaData.getColumns(null, null, tableName, null)));
			return tableMetadata;
		});
	}

	@Override
	public void close() throws Exception {
		connection.close();
	}
}

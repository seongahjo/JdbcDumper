package com.ahjo.jdbc.dumper.executor;

import com.ahjo.jdbc.dumper.Row;
import com.ahjo.jdbc.dumper.TableMetadata;
import com.ahjo.jdbc.dumper.sql.SelectSql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * Execute query command by JDBC driver
 *
 * @author seongahjo
 */
public interface Executor extends AutoCloseable {
	/**
	 * connect to database by given driver
	 *
	 * @param driverClassName driver class name
	 * @param url             jdbc url
	 * @param userName        user name
	 * @param password        password
	 */
	default Connection connect(String driverClassName, String url, String userName, String password) {
		Connection connection = null;
		try {
			Class.forName(driverClassName);
			connection = DriverManager.getConnection(url, userName, password);
		} catch (ClassNotFoundException e) {
		} catch (SQLException e) {
		} finally {
			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return connection;
	}

	/**
	 * find tables in given database
	 *
	 * @param databaseName database name
	 * @return <code>List<String></code> a list of table names
	 */
	List<String> showTables(String databaseName);


	TableMetadata inspectTable(String databaseName, String tableName);

	/**
	 * query data by given sql
	 *
	 * @param databaseName database name
	 * @param sql          sql to execute
	 * @return <code>List<BaseRow></code> rows in given database by given sql
	 */
	List<Row> query(String databaseName, SelectSql sql);
}

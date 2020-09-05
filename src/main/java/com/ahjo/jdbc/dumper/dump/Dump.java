package com.ahjo.jdbc.dumper.dump;

import com.ahjo.jdbc.dumper.generator.QueryOption;
import com.ahjo.jdbc.dumper.sql.InsertSql;

import java.util.List;

/**
 * Dump database or table by JDBC driver
 *
 * @author seongahjo
 */
public interface Dump extends AutoCloseable {
	/**
	 * dump all tables' rows in database
	 *
	 * @param databaseName name of database to dump
	 * @return <code>List<InsertSql></code> dump rows in database
	 */
	List<InsertSql> dump(String databaseName);

	/**
	 * dump table's rows
	 *
	 * @param databaseName name of database to dump
	 * @param tableName    name of table to dump
	 * @param options      options for filtering rows
	 * @return <code>List<InsertSql></code> dump rows in database
	 */
	List<InsertSql> dump(String databaseName, String tableName, QueryOption... options);
}

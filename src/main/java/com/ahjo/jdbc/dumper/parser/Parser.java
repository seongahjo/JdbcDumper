package com.ahjo.jdbc.dumper.parser;

import com.ahjo.jdbc.dumper.ColumnInfo;
import com.ahjo.jdbc.dumper.Row;

import java.sql.ResultSet;
import java.util.List;

/**
 * Parse query result to generate table metadata and row
 * a way of parsing or generating data can be different depending on databases
 *
 * @author seongahjo
 */
public interface Parser {
	/**
	 * parse table column information from jdbc connection metadata
	 *
	 * @param resultSet <code>ResultSet</code> contains column information
	 * @return <code>List<ColumnInfo></code> a list of columnInfo
	 */
	List<ColumnInfo> parseColumnInfo(ResultSet resultSet);

	/**
	 * parse table row from JDBC <code>ResultSet</code>
	 *
	 * @param resultSet <code>ResultSet</code> contains query result
	 * @return <code>List<BaseRow></code> row data list
	 */
	List<Row> parseRow(ResultSet resultSet);


	/**
	 * parse table names from JDBC <code>ResultSet</code>
	 *
	 * @param resultSet <code>ResultSet</code> contains table names
	 * @return <code>List<String></code> a list of table names
	 */
	List<String> parseTableNames(ResultSet resultSet);
}

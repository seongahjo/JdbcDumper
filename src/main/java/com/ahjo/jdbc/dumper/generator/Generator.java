package com.ahjo.jdbc.dumper.generator;

import com.ahjo.jdbc.dumper.Row;
import com.ahjo.jdbc.dumper.TableMetadata;
import com.ahjo.jdbc.dumper.sql.InsertSql;
import com.ahjo.jdbc.dumper.sql.SelectSql;

import java.util.ArrayList;
import java.util.List;

/**
 * Generate Sql by given data
 *
 * @author seongahjo
 */
public interface Generator {
	/**
	 * generate select query
	 *
	 * @param tableMetadata table metadata
	 * @param options       options for select query
	 * @return <code>SelectSql</code> select query
	 */
	SelectSql select(TableMetadata tableMetadata, QueryOption... options);

	/**
	 * generate insert query
	 *
	 * @param row           row to convert into insert query
	 * @param tableMetadata table metadata
	 * @return <code>InsertSql</code> insert query
	 */
	InsertSql insert(Row row, TableMetadata tableMetadata);

	default List<InsertSql> insert(List<Row> rows, TableMetadata tableMetadata) {
		List<InsertSql> sqlList = new ArrayList<>();
		for (Row row : rows) {
			sqlList.add(insert(row, tableMetadata));
		}
		return sqlList;
	}
}

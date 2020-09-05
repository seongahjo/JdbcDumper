package com.ahjo.jdbc.dumper.dump;

import com.ahjo.jdbc.dumper.Row;
import com.ahjo.jdbc.dumper.TableMetadata;
import com.ahjo.jdbc.dumper.executor.Executor;
import com.ahjo.jdbc.dumper.generator.Generator;
import com.ahjo.jdbc.dumper.generator.QueryOption;
import com.ahjo.jdbc.dumper.sql.InsertSql;
import com.ahjo.jdbc.dumper.sql.SelectSql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractDump implements Dump {
	protected final Executor executor;
	protected final Generator generator;

	public AbstractDump(Executor executor, Generator generator) {
		this.executor = executor;
		this.generator = generator;
	}

	private List<InsertSql> dumpEach(String databaseName, TableMetadata tableMetadata, QueryOption... options) {
		SelectSql selectSql = generator.select(tableMetadata, options);
		List<Row> rows = executor.query(databaseName, selectSql);
		return generator.insert(rows, tableMetadata);
	}

	private List<InsertSql> dump(String databaseName, Map<String, TableMetadata> metadataMap, QueryOption... options) {
		List<InsertSql> insertSqlList = new ArrayList<>();
		if (metadataMap.isEmpty()) {
			throw new IllegalStateException("table not exists");
		}
		for (TableMetadata metadata : metadataMap.values()) {
			insertSqlList.addAll(dumpEach(databaseName, metadata, options));
		}
		return insertSqlList;
	}

	@Override
	public List<InsertSql> dump(String databaseName, String tableName, QueryOption... options) {
		TableMetadata tableMetadata = executor.inspectTable(databaseName, tableName);
		Map<String, TableMetadata> tableMetadataMap = new HashMap<>();
		tableMetadataMap.put(tableName, tableMetadata);
		return dump(databaseName, tableMetadataMap, options);
	}

	@Override
	public List<InsertSql> dump(String databaseName) {
		List<String> tableNames = executor.showTables(databaseName);
		Map<String, TableMetadata> tableMetadataMap = new HashMap<>();
		for (String tableName : tableNames) {
			TableMetadata tableMetadata = executor.inspectTable(databaseName, tableName);
			tableMetadataMap.put(tableName, tableMetadata);

		}
		return dump(databaseName, tableMetadataMap);
	}

	@Override
	public void close() throws Exception {
		executor.close();
	}
}

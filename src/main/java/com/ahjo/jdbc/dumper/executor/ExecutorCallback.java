package com.ahjo.jdbc.dumper.executor;

import java.sql.SQLException;

@FunctionalInterface
public interface ExecutorCallback<T, R> {
	R execute(T t) throws SQLException;
}

package com.ahjo.jdbc.dumper.parser;

import com.ahjo.jdbc.dumper.ColumnInfo;

public interface TypeMatcher {
	ColumnInfo.DataType match(String dataType);
}

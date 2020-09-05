package com.ahjo.jdbc.dumper.parser;

import com.ahjo.jdbc.dumper.ColumnInfo;
import com.ahjo.jdbc.dumper.Row;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ParserTest {
	private static ResultSet actualRowResultSet;
	private static List<Row> expectedRow;
	private static ResultSet actualTableNamesResultSet;
	private static final List<String> expectedTableNames;
	private static ResultSet actualColumnInfoResultSet;
	private static final List<ColumnInfo> expectedColumnInfo;
	private static Parser parser;

	static {
		expectedTableNames = Lists.newArrayList("student", "university");
		expectedColumnInfo = Lists.newArrayList(new ColumnInfo("name", ColumnInfo.DataType.STRING),
				new ColumnInfo("id", ColumnInfo.DataType.STRING));
		expectedRow = new ArrayList<>();
		Row row = new Row().put("name", "seongahjo").put("id", "1");
		expectedRow.add(row);
	}

	private static class TestTypeMatcher implements TypeMatcher {
		@Override
		public ColumnInfo.DataType match(String dataType) {
			return ColumnInfo.DataType.STRING;
		}
	}


	@BeforeAll
	static void beforeAll() throws SQLException {
		mockRow();
		mockTableNames();
		mockColumnInfo();
	}

	@Test
	void defaultParseRow() {
		parser = new DefaultParser(new TestTypeMatcher());
		List<Row> actual = parser.parseRow(actualRowResultSet);
		assertThat(actual).isEqualTo(expectedRow);
	}

	@Test
	void defaultParseTableNames() {
		parser = new DefaultParser(new TestTypeMatcher());
		List<String> actual = parser.parseTableNames(actualTableNamesResultSet);
		assertThat(actual).isEqualTo(expectedTableNames);
	}

	@Test
	void defaultColumnInfo() {
		parser = new DefaultParser(new TestTypeMatcher());
		List<ColumnInfo> actual = parser.parseColumnInfo(actualColumnInfoResultSet);
		assertThat(actual).isEqualTo(expectedColumnInfo);
	}

	private static void mockColumnInfo() throws SQLException {
		actualColumnInfoResultSet = mock(ResultSet.class);
		when(actualColumnInfoResultSet.next()).thenAnswer(getAnswerLimitSize(2));
		when(actualColumnInfoResultSet.getString("COLUMN_NAME"))
				.thenAnswer(getAnswerValueFromList(ColumnInfo::getColumnName, expectedColumnInfo));
		when(actualColumnInfoResultSet.getString("DATA_TYPE"))
				.thenAnswer(getAnswerValueFromList(c -> c.getDataType().name(), expectedColumnInfo));
	}

	private static void mockTableNames() throws SQLException {
		actualTableNamesResultSet = mock(ResultSet.class);
		when(actualTableNamesResultSet.next()).thenAnswer(getAnswerLimitSize(expectedTableNames.size()));
		when(actualTableNamesResultSet.getString("TABLE_NAME"))
				.thenAnswer(getAnswerValueFromList(s -> s, expectedTableNames));
	}

	private static void mockRow() throws SQLException {
		actualRowResultSet = mock(ResultSet.class);
		ResultSetMetaData resultSetMetaData = mock(ResultSetMetaData.class);
		when(actualRowResultSet.next()).thenAnswer(getAnswerLimitSize(1));
		when(resultSetMetaData.getColumnCount()).thenReturn(expectedColumnInfo.size());
		when(resultSetMetaData.getColumnName(0)).thenReturn(expectedColumnInfo.get(0).getColumnName());
		when(resultSetMetaData.getColumnName(1)).thenReturn(expectedColumnInfo.get(1).getColumnName());
		when(actualRowResultSet.getMetaData()).thenReturn(resultSetMetaData);
		when(actualRowResultSet.getString(0)).thenReturn("seongahjo");
		when(actualRowResultSet.getString(1)).thenReturn("1");
	}

	private static Answer<Boolean> getAnswerLimitSize(final int size) {
		return new Answer<Boolean>() {
			private int count = 0;

			public Boolean answer(InvocationOnMock invocation) {
				return count++ < size;
			}
		};
	}

	private static <T, R> Answer<R> getAnswerValueFromList(Function<T, R> func, List<T> list) {
		return new Answer<R>() {
			private int count = 0;

			public R answer(InvocationOnMock invocation) {
				return func.apply(list.get(count++));
			}
		};
	}
}

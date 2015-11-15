package it.balyfix.java8.lambdas;

import it.balyfix.stock.model.Stock;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

public class SpringJdbc {

	private EmbeddedDatabase db;

	private JdbcTemplate jdbcTemplate;
	private DataSourceTransactionManager transactionManager;

	@After
	public void tearDown() throws Exception {
		if (db != null) {
			db.shutdown();
		}
	}

	@Before
	public void initDb() {
		db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.DERBY)
				.addScript("sql/init.sql").build();
		jdbcTemplate = new JdbcTemplate(db);
		transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(db);
	}

	@Test
	public void queryLabdasExamples() {
		List<Stock> query = jdbcTemplate.query(
				"SELECT * FROM STOCK",
				(rs, i) -> new Stock(rs.getString("STOCK_CODE"), rs
						.getString("STOCK_NAME")));
		// little over engineer for checking the list size ;-)
		Assert.assertEquals(query.stream().count(), 2);
		Function<List<Stock>, Integer> checkSize = x -> Integer.valueOf(x
				.size());
		Assert.assertTrue(checkSize.apply(query) == 2);

	}

	@Test
	public void doInConnectionExamples() {
		//
		// jdbcTemplate.execute(new ConnectionCallback<Boolean>() {
		//
		// @Override
		// public Boolean doInConnection(Connection arg0) throws SQLException,
		// DataAccessException {
		// // TODO Auto-generated method stub
		// return null;
		// }
		// }) ;

		jdbcTemplate.execute((Connection con) -> {
			con.close();
			return false;
		});

		Boolean execute = jdbcTemplate.execute((Connection conn) -> {
			DatabaseMetaData dbMetadata = conn.getMetaData();
			ResultSet rs = dbMetadata.getSchemas();
			boolean next = rs.next();
			rs.close();
			return next;
		});
		Assert.assertTrue(execute);

	}

	@Test
	public void doInTrasactinExamples() {

		TransactionTemplate transactionTemplate = new TransactionTemplate(
				transactionManager);
		// transactionTemplate.execute(new TransactionCallbackWithoutResult() {
		//
		// @Override
		// protected void doInTransactionWithoutResult(TransactionStatus arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// });TransactionStatus

		transactionTemplate.execute((TransactionStatus ts) -> {

			// do something

				return ts;

			});

	}

}

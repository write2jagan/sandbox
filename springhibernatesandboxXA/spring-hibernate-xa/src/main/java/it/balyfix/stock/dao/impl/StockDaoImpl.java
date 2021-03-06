package it.balyfix.stock.dao.impl;

import it.balyfix.stock.dao.StockDao;
import it.balyfix.stock.model.Stock;
import it.balyfix.util.CustomHibernateDaoSupport;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("stockDao")
public class StockDaoImpl extends CustomHibernateDaoSupport implements StockDao {

	public void save(Stock stock) {
		getHibernateTemplate().save(stock);
	}

	public void update(Stock stock) {
		getHibernateTemplate().update(stock);
	}

	public void delete(Stock stock) {
		getHibernateTemplate().delete(stock);
	}

	public Stock findByStockCode(String stockCode) {
		List list = getHibernateTemplate().find("from Stock where stockCode=?",
				stockCode);
		return (Stock) list.get(0);
	}

}
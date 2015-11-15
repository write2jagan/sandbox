package it.balyfix.stock.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "stock", uniqueConstraints = {
		@UniqueConstraint(columnNames = "STOCK_NAME"),
		@UniqueConstraint(columnNames = "STOCK_CODE") })
public class Stock implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3619467232792095673L;
	private Integer stockId;
	private String stockCode;
	private String stockName;
	private StockDetail stockDetail;

	public Stock() {
	}

	public Stock(String stockCode, String stockName) {
		this.stockCode = stockCode;
		this.stockName = stockName;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "STOCK_ID", nullable = false)
	public Integer getStockId() {
		return this.stockId;
	}

	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}

	@Column(name = "STOCK_CODE", nullable = false, length = 10)
	public String getStockCode() {
		return this.stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	@Column(name = "STOCK_NAME", nullable = false, length = 20)
	public String getStockName() {
		return this.stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "stock", cascade = CascadeType.ALL)
	public StockDetail getStockDetail() {
		return stockDetail;
	}

	public void setStockDetail(StockDetail stockDetail) {
		this.stockDetail = stockDetail;
	}

}
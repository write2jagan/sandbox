package it.balyfix.stock.model;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;


public class Stock implements java.io.Serializable
{

    /**
	 * 
	 */
    private static final long serialVersionUID = -3619467232792095673L;

    private Integer stockId;

    private String stockCode;

    private String stockName;

    private StockDetail stockDetail;

    private Boolean isValid;
    
    
    private Random random ;

    public Stock(String stockName)
    {
        this.stockName = stockName;
        random = new Random(stockName.charAt(0) * stockName.charAt(1) * stockName.charAt(2));
    }

    public Stock(String stockCode, String stockName)
    {
        this.stockCode = stockCode;
        this.stockName = stockName;
    }

    public Integer getStockId()
    {
        return this.stockId;
    }

    public void setStockId(Integer stockId)
    {
        this.stockId = stockId;
    }

    public Optional<String> getStockCode()
    {
        return  Optional.ofNullable(this.stockCode);
    }

    public void setStockCode(String stockCode)
    {
        this.stockCode = stockCode;
    }

    public String getStockName()
    {
        return this.stockName;
    }

    public void setStockName(String stockName)
    {
        this.stockName = stockName;
    }

    public StockDetail getStockDetail()
    {
        return stockDetail;
    }

    public void setStockDetail(StockDetail stockDetail)
    {
        this.stockDetail = stockDetail;
    }

    public Boolean isValid()
    {
        return isValid;
    }

    public void setIsValid(Boolean isValid)
    {
        this.isValid = isValid;
    }

    public BigDecimal getValue()
    {
        dalay();
        return BigDecimal.valueOf(random.nextDouble());
    }
    
    public Double getDouble()
    {
        return random.nextDouble();
    }
    

    // Horrible put in data object some logic but this is for test only

    private void dalay()
    {
        try
        {
            Thread.sleep(10000);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
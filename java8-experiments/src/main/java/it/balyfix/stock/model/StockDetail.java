package it.balyfix.stock.model;

import java.time.LocalDate;


public class StockDetail implements java.io.Serializable
{

    /**
	 * 
	 */
    private static final long serialVersionUID = -425535484118551469L;

    private Stock stock;

    private String compName;

    private String compDesc;

    private String remark;

    private LocalDate date;

    public StockDetail()
    {
    }

    public StockDetail(Stock stock, String compName, String compDesc, String remark, LocalDate date)
    {
        this.stock = stock;
        this.compName = compName;
        this.compDesc = compDesc;
        this.remark = remark;
        this.date = date;
    }

    public Stock getStock()
    {
        return this.stock;
    }

    public void setStock(Stock stock)
    {
        this.stock = stock;
    }

    public String getCompName()
    {
        return this.compName;
    }

    public void setCompName(String compName)
    {
        this.compName = compName;
    }

    public String getCompDesc()
    {
        return this.compDesc;
    }

    public void setCompDesc(String compDesc)
    {
        this.compDesc = compDesc;
    }

    public String getRemark()
    {
        return this.remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

}
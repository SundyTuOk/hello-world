package com.sf.energy.water.manager.current.model;

public class WaterQuotaManagerModel
{
	// /统计年份
	private String year = "2014";
	// /区域建筑名称
	private String name = "";
	// /总分配定额(元)
	private String totalQuota = "";
	// /按标准分配定额(元)
	private String standardQuota = "";
	// /调整定额(元)
	private String TiaozhengFixed = "";
	// /每月追加总定额(元)
	private String addPerMonthQuota = "";
	// /已用定额量(元)
	private String ZMoney = "";
	// /剩余定额量(元)
	private String remainQuota = "";

	///值ID
	private int valueID;
	// /用水名称
	private String userAmStyle = "";
	// /标准统计
	private String Biaozhun = "";
	// /总定额量(吨)
	private String countFixedGross = "";
	// /适用单价
	private String price = "";
	// /总定额费用(元)
	private String countFixedMoney = "";
	// /备注信息
	private String remark = "";
	public String getYear()
	{
		return year;
	}
	public void setYear(String year)
	{
		this.year = year;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getTotalQuota()
	{
		return totalQuota;
	}
	public void setTotalQuota(String totalQuota)
	{
		this.totalQuota = totalQuota;
	}
	public String getStandardQuota()
	{
		return standardQuota;
	}
	public void setStandardQuota(String standardQuota)
	{
		this.standardQuota = standardQuota;
	}
	public String getTiaozhengFixed()
	{
		return TiaozhengFixed;
	}
	public void setTiaozhengFixed(String tiaozhengFixed)
	{
		TiaozhengFixed = tiaozhengFixed;
	}
	public String getAddPerMonthQuota()
	{
		return addPerMonthQuota;
	}
	public void setAddPerMonthQuota(String addPerMonthQuota)
	{
		this.addPerMonthQuota = addPerMonthQuota;
	}
	public String getZMoney()
	{
		return ZMoney;
	}
	public void setZMoney(String zMoney)
	{
		ZMoney = zMoney;
	}
	public String getRemainQuota()
	{
		return remainQuota;
	}
	public void setRemainQuota(String remainQuota)
	{
		this.remainQuota = remainQuota;
	}
	public int getValueID()
	{
		return valueID;
	}
	public void setValueID(int valueID)
	{
		this.valueID = valueID;
	}
	public String getUserAmStyle()
	{
		return userAmStyle;
	}
	public void setUserAmStyle(String userAmStyle)
	{
		this.userAmStyle = userAmStyle;
	}
	public String getBiaozhun()
	{
		return Biaozhun;
	}
	public void setBiaozhun(String biaozhun)
	{
		Biaozhun = biaozhun;
	}
	public String getCountFixedGross()
	{
		return countFixedGross;
	}
	public void setCountFixedGross(String countFixedGross)
	{
		this.countFixedGross = countFixedGross;
	}
	public String getPrice()
	{
		return price;
	}
	public void setPrice(String price)
	{
		this.price = price;
	}
	public String getCountFixedMoney()
	{
		return countFixedMoney;
	}
	public void setCountFixedMoney(String countFixedMoney)
	{
		this.countFixedMoney = countFixedMoney;
	}
	public String getRemark()
	{
		return remark;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	
	

}
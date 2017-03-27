/**
 * Copyright  All right reserved by IZHUO.NET.
 */
package net.izhuo.app.library.entity;

/**
 * @author Changlei
 *
 *         2014年10月26日
 */
public class IHotAddress {
	
	private String name;
	private String pinYinName;
	private String province;

	public IHotAddress(String name) {
		super();
		this.name = name;
	}

	public IHotAddress(String name, String pinYinName) {
		super();
		this.name = name;
		this.pinYinName = pinYinName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinYinName() {
		return pinYinName;
	}

	public void setPinYinName(String pinYinName) {
		this.pinYinName = pinYinName;
	}

	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * @param province
	 *            the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}
}

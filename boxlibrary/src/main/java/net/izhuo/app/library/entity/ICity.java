/*
 * Copyright © All right reserved by CHANGLEI.
 */

package net.izhuo.app.library.entity;

/**
 * @author Changlei
 *
 *         2014年10月27日
 */
public class ICity {

	private String cityName;
	private String Province;
	private String Value;
	private String zipCode;

	/**
	 * @return the cityName
	 */
	public String getCityName() {
		return cityName;
	}

	/**
	 * @param cityName
	 *            the cityName to set
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	/**
	 * @return the province
	 */
	public String getProvince() {
		return Province;
	}

	/**
	 * @param province
	 *            the province to set
	 */
	public void setProvince(String province) {
		Province = province;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return Value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		Value = value;
	}

	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * @param zipCode
	 *            the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
}

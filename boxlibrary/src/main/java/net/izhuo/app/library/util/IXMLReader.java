/**
 * Copyright  All right reserved by IZHUO.NET.
 */
package net.izhuo.app.library.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;

import net.izhuo.app.library.R;
import net.izhuo.app.library.entity.ICity;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Changlei
 *
 *         2014年8月8日
 */
public class IXMLReader {

	private static final String PID = "PID";
	private static final String CITY_NAME = "CityName";
	private static final String ID = "ID";
	private static final String PROVINCE_NAME = "ProvinceName";
	private static final String ZIP_CODE = "ZipCode";

	public static Map<String, List<ICity>> getXMLCity(Context context,
													  int res) {
		Map<String, List<ICity>> addressMap = new HashMap<String, List<ICity>>();
		Map<String, String> xmlProvinces = getXMLProvinces(context,
				R.xml.box_provinces);
		Resources resources = context.getResources();
		XmlResourceParser xrp = resources.getXml(res);
		try {
			while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
				if (xrp.getEventType() == XmlResourceParser.START_TAG) {
					String pid = xrp.getAttributeValue(null, PID);
					String zipCode = xrp.getAttributeValue(null, ZIP_CODE);
					String provinceName = getProvinceName(context,
							xmlProvinces, pid);
					if (!TextUtils.isEmpty(pid)) {
						String cityName = xrp
								.getAttributeValue(null, CITY_NAME);
						List<ICity> cities = addressMap.get(cityName);
						if (cities == null) {
							cities = new ArrayList<ICity>();
						}
						if (!TextUtils.isEmpty(cityName)) {
							ICity city = new ICity();
							city.setCityName(cityName);
							city.setProvince(provinceName);
							city.setValue(cityName);
							city.setZipCode(zipCode);
							cities.add(city);
							addressMap.put(cityName, cities);
						}
					}
				}
				xrp.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return addressMap;
	}

	private static String getProvinceName(Context context,
			Map<String, String> map, String pid) {
		return map.get(pid);
	}

	public static Map<String, String> getXMLProvinces(Context context, int res) {
		Map<String, String> addressMap = new LinkedHashMap<String, String>();
		Resources resources = context.getResources();
		XmlResourceParser xrp = resources.getXml(res);
		try {
			while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
				if (xrp.getEventType() == XmlResourceParser.START_TAG) {
					String id = xrp.getAttributeValue(null, ID);
					String province = xrp
							.getAttributeValue(null, PROVINCE_NAME);
					if (!TextUtils.isEmpty(id) && province != null
							&& !TextUtils.isEmpty(province)) {
						addressMap.put(id, province);
					}
				}
				xrp.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return addressMap;
	}
	
}

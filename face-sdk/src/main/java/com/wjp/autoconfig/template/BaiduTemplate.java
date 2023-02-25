package com.wjp.autoconfig.template;

import com.alibaba.fastjson.JSONObject;
import com.wjp.autoconfig.properties.BaiduMapProperties;
import lombok.extern.slf4j.Slf4j;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalCoordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BaiduTemplate {

    private BaiduMapProperties baiduMapProperties;

    public BaiduTemplate(BaiduMapProperties properties) {
        this.baiduMapProperties = properties;
    }


    /**
     * 根据经纬度调用百度API获取 地理位置信息，根据经纬度
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return
     */
    public String getAddressInfoByLngAndLat(String longitude, String latitude) {
        String location = latitude + "," + longitude;
        //百度url  coordtype :bd09ll（百度经纬度坐标）、bd09mc（百度米制坐标）、gcj02ll（国测局经纬度坐标，仅限中国）、wgs84ll（ GPS经纬度）
        String url = "http://api.map.baidu.com/reverse_geocoding/v3/?ak="
                + baiduMapProperties.getAk() + "&output=json&coordtype=wgs84ll&location=" + location;
        try {
            String json = getResultByUrl(url);
            JSONObject obj = JSONObject.parseObject(json);
            System.out.println(obj.toString());
            // status:0 成功
            String success = "0";
            String status = String.valueOf(obj.get("status"));
            if (success.equals(status)) {
                String result = String.valueOf(obj.get("result"));
                JSONObject resultObj = JSONObject.parseObject(result);
                //JSON字符串转换成Java对象
                log.info("getAddress:{}", result);
                return resultObj.getString("formatted_address");
            }
        } catch (Exception e) {
            log.error("未找到相匹配的经纬度，请检查地址！error: {}", e.getMessage());
        }
        return null;
    }


    /**
     * 百度地图通过地址来获取经纬度，传入参数address
     *
     * @param address
     * @return
     */
    public Map<String, Double> getLngAndLat(String address) {
        Map<String, Double> map = new HashMap<String, Double>();
        String url = "http://api.map.baidu.com/geocoding/v3/?address="
                + address + "&output=json&ak=" + baiduMapProperties.getAk() + "&callback=showLocation";
        String json = loadJSON(url);
        JSONObject obj = JSONObject.parseObject(json);
        if (obj.get("status").toString().equals("0")) {
            double lng = obj.getJSONObject("result").getJSONObject("location").getDouble("lng");
            double lat = obj.getJSONObject("result").getJSONObject("location").getDouble("lat");
            map.put("jd", lng);
            map.put("wd", lat);
            log.info("经度：" + lng + "--- 纬度：" + lat);
        } else {
            log.error("未找到相匹配的经纬度！");
        }
        return map;
    }


    /**
     * 通过经纬度获取距离(单位：米)
     *
     * @param longitudeFrom
     * @param latitudeFrom
     * @param longitudeTo
     * @param latitudeTo
     * @return
     */
    public double getDistance(double longitudeFrom, double latitudeFrom, double longitudeTo, double latitudeTo) {
        GlobalCoordinates source = new GlobalCoordinates(latitudeFrom, longitudeFrom);
        GlobalCoordinates target = new GlobalCoordinates(latitudeTo, longitudeTo);

        return new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.Sphere, source, target).getEllipsoidalDistance();
    }



    public static String getResultByUrl(String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return json.toString();
    }

    public static String loadJSON(String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream(), "UTF-8"));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
        }
        int index1 = json.indexOf("(");
        int index2 = json.lastIndexOf(")");

        return json.substring(index1 + 1, index2);
    }

}

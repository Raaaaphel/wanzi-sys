package controllers;

import models.Order;
import play.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import play.libs.Json;
import play.libs.XPath;
import play.mvc.Controller;
import play.mvc.Result;
import util.payment.Util;
import util.payment.HttpRequest;
import util.payment.PayUtil;
import util.payment.XMLParser;

public class PayController extends Controller {


    //code 微信返回的code
    //out_trade_no 此处指订单ID
    //total_fee 订单需要支付的金额
    //appid、secret 参考文档
    //notify_url 支付成功之后 微信会进行异步回调的地址

    public static Result submitPayWx() {
        Map<String, String[]> formData = request().body().asFormUrlEncoded();
        String ip = getIpAddr();
        String out_trade_no = formData.get("orderId")[0];
        Order order = Order.get(out_trade_no);
        try {
            String nonce_str = PayUtil.getRandomStringByLength(16);//生成随机数，可直接用系统提供的方法
            String trade_type = "MWEB";
            String appid = "wx806bf0ac9a8bd94e";
            String mch_id = "1491635342";
            String notify_url = "http://test.izouzou.cc/pay/WxPaidNotify";
            String scene_info = "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"https://test.izouzou.cc\",\"wap_name\": \"走走旅游\"}}";
            Map<String, Object> map = new HashMap<>();
            map.put("appid", appid);
            map.put("mch_id", mch_id);
            map.put("device_info", "WEB");
            map.put("nonce_str", nonce_str);
            map.put("body", order.serviceType);//订单标题
            map.put("out_trade_no", out_trade_no);//订单ID
            //map.put("total_fee", order.total_price); //需支付金额
            map.put("total_fee", 1);//订单需要支付的金额
            map.put("spbill_create_ip", ip);
            map.put("trade_type", trade_type);
            map.put("notify_url", notify_url);//notify_url 支付成功之后 微信会进行异步回调的地址
            map.put("scene_info", scene_info); //场景信息
            String sign = PayUtil.getSign(map);//参数加密  该方法key的需要根据你当前公众号的key进行修改
            map.put("sign", sign);
            String content = XMLParser.getXMLFromMap(map);
            HttpRequest http = new HttpRequest();
            //调用统一下单接口
            String PostResult = http.sendPost("https://api.mch.weixin.qq.com/pay/unifiedorder", content);
            Map<String, Object> cbMap = XMLParser.getMapFromXML(PostResult);
            if (cbMap.get("return_code").equals("SUCCESS") && cbMap.get("result_code").equals("SUCCESS")) {

                String prepay_id = cbMap.get("prepay_id") + "";//这就是预支付id
                String mwebUrl = cbMap.get("mweb_url") + ""; //拉起微信客户端的跳转url
                Map<String, Object> result = new HashMap<>();
                result.put("mweb_url", mwebUrl);
                return ok(Json.toJson(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ok("submitPayWx");
    }

    //支付回调接口（微信异步会通知）notify_url 配置的值
    public static Result payCallback() {
        Document xmlData = request().body().asXml();
        String resultCode = XPath.selectText("//result_code", xmlData);
        String endTime = XPath.selectText("//time_end", xmlData);
        String orderId = XPath.selectText("//out_trade_no", xmlData);
        String sign = XPath.selectText("//sign", xmlData);
        if (resultCode.equals("SUCCESS")) {
            Logger.info("order:"+orderId);
            Logger.info("time_end"+endTime);
            Logger.info("sign:"+sign);
        }
        //返回的数据

        //支付回调处理订单 更改订单状态
        String xml = "<xml>"
                + "<return_code><![CDATA[SUCCESS]]></return_code>"
                + "<return_msg><![CDATA[OK]]></return_msg>"
                + "</xml>";
        return ok(xml).as("text/xml");
    }

    private static String getIpAddr() {
        String ipAddress = null;
        //ipAddress = request().getRemoteAddr();
       /* Map<String, String[]> requestHeaders = request().headers();
        requestHeaders.keySet().forEach((e)->{
            Logger.info(e+":"+Arrays.toString(requestHeaders.get(e)));
        });*/
        ipAddress = request().getHeader("X-Forwarded-For");
        //Logger.info("x-forwarded-for"+ipAddress);
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request().getHeader("Proxy-Client-IP");
           // Logger.info("Proxy-Client-IP"+ipAddress);
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request().getHeader("WL-Proxy-Client-IP");
            //Logger.info("WL-Proxy-Client-IP"+ipAddress);
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request().remoteAddress();
           // Logger.info("remoteAddress"+ipAddress);
            if (ipAddress.equals("127.0.0.1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }

        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) {
            //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
               // Logger.info("多代理"+ipAddress);
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }
}




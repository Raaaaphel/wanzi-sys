package controllers;

import models.AUser;
import models.DemandOrder;
import models.GroupOrder;
import models.Order;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import service.MailService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class OrderController extends Controller {
    /**
     * 定制旅行
     * uri /site/demand
     */
    public static Result demandPage() {
        AUser u = AUser.getUserById(session("userId"));
        if (u == null) {
            return RegisterController.onLogout("请先登录!");
        }

        return ok(views.html.demand.render());
    }

    /**
     * uri /intent/addOlister
     * 提交旅行计划
     */
    public static Result onDemand() {
        Map<String, String[]> formData = request().body().asFormUrlEncoded();
        MailService mailService = new MailService();

        DemandOrder order = new DemandOrder();
        order.setUserId(session("userId"));
        AUser u = AUser.getUserById(session("userId"));
        order.setStartTime(formData.get("start")[0]);
        order.setEndTime(formData.get("end")[0]);
        order.setTravelArea(formData.get("area[]"));
        order.setCar(Integer.parseInt(formData.get("car")[0]));
        order.setPick(Integer.parseInt(formData.get("pick")[0]));
        order.setContent(formData.get("content")[0]);
        order.setPersonNum(Integer.parseInt(formData.get("person")[0]));
        DemandOrder.addDemandOrder(order);
        String title = "个人定制_" + u.name + "_" + u.email;
        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("<html><body><p>用户名称：");
        bodyBuilder.append(u.name);
        bodyBuilder.append("</p><p>邮箱：");
        bodyBuilder.append(u.email);
        bodyBuilder.append("</p><p>出行日期：");
        bodyBuilder.append(order.getStartTime() + "至" + order.getEndTime());
        bodyBuilder.append("</p><p>出行地点：");
        String[] areas = order.getTravelArea();
        for (int i = 0; i < areas.length; i++) {
            bodyBuilder.append(areas[i] + "  ");
        }
        bodyBuilder.append("</p><p>出行人数：");
        bodyBuilder.append(order.getPersonNum());
        bodyBuilder.append("</p><p>是否需要用车：");
        if (order.getCar().equals(1)) {
            bodyBuilder.append("是");
        } else {
            bodyBuilder.append("否");
        }
        bodyBuilder.append("</p><p>是否需要接送机：");
        if (order.getCar().equals(1)) {
            bodyBuilder.append("是");
        } else {
            bodyBuilder.append("否");
        }
        bodyBuilder.append("</p><p>详细需求：");
        bodyBuilder.append(order.getContent());
        bodyBuilder.append("</p></body><html>");
        mailService.sendGroupDemandMail(title, bodyBuilder.toString());
        Map<String, Object> result = new HashMap<>();
        result.put("code", 1000);
        return ok(Json.toJson(result));
    }

    /**
     * uri /site/group
     */
    public static Result groupDemand() {
        return ok(views.html.group.render());
    }

    /**
     * uri /intent/addFirm
     * 提交团队旅行计划，向工作邮箱中发送邮件
     */
    public static Result onGroupDemand() {
        MailService mailService = new MailService();
        Map<String, String[]> formData = request().body().asFormUrlEncoded();
        GroupOrder order = new GroupOrder();
        order.setContent(formData.get("content")[0]);
        order.setFirmName(formData.get("firm_name")[0]);
        order.setPhone(formData.get("phone")[0]);
        order.setUserName(formData.get("user_name")[0]);
        order.setUserEmail(formData.get("user_email")[0]);
        String title = "团游定制_" + order.getFirmName() + "_" + order.getUserName();
        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("<html><body><p>企业名称：");
        bodyBuilder.append(order.getFirmName());
        bodyBuilder.append("</p><p>联系人：");
        bodyBuilder.append(order.getUserName());
        bodyBuilder.append("</p><p>联系电话：");
        bodyBuilder.append(order.getPhone());
        bodyBuilder.append("</p><p>邮箱：");
        bodyBuilder.append(order.getUserEmail());
        bodyBuilder.append("</p><p>需求内容：");
        bodyBuilder.append(order.getContent());
        bodyBuilder.append("</p></body><html>");
        mailService.sendGroupDemandMail(title, bodyBuilder.toString());
        Map<String, Object> result = new HashMap<>();
        result.put("code", 1000);
        return ok(Json.toJson(result));
    }

    public static Result getBalance() {
        String userId = session("userId");
        AUser user = AUser.getUserById(session("userId"));
        if (user == null) {
            return RegisterController.onLogout("请先登录!");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("balance", user.balance);
        return ok(Json.toJson(result));
    }

    public static Result booking(String uid, String orderId) {
        AUser guider = AUser.getUserById(uid);
        if (orderId == null) {
            String customerId = session("userId");
            Order order = Order.getByCustomerAndGuider(customerId, uid);
            return ok(views.html.orderPage.booking.render(guider, order));
        } else {
            Order order = Order.get(orderId);
            return ok(views.html.orderPage.booking.render(guider, order));
        }
    }

    public static Result addService(String uid) {
        AUser guider = AUser.getUserById(uid);
        return ok(views.html.orderPage.addService.render(guider));
    }

    public static Result wizardService(String uid, String priceType) {
        AUser guider = AUser.getUserById(uid);
        String price = "";
        /*switch (type){
            case "guider_price":
                price = guider.guider_price;
                break;
            case "guiderdrive_price":
                price = guider.guiderdrive_price;
                break;
            case "guiderpickup_price":
                price = guider.guiderpickup_price;
                break;
        }*/
        return ok(views.html.orderPage.wizardService.render(guider, priceType));
    }

    public static Result beforeBooking(String uid) {
        return ok(views.html.orderPage.beforeBooking.render(uid));
    }

    public static Result payCenter(String orderId) {
        Order order = Order.get(orderId);
        return ok(views.html.orderPage.payCenter.render(order));
    }

    public static Result createOrder() {
        Map<String, String[]> formData = request().body().asFormUrlEncoded();
        String guiderId = formData.get("guiderId")[0];
        String travellerId = session("userId");
        String priceType = formData.get("priceType")[0];
        String startTime = formData.get("startTime")[0];
        String endTime = formData.get("endTime")[0];
        Integer num = Integer.parseInt(formData.get("num")[0]);
        AUser guider = AUser.getUserById(guiderId);
        AUser traveller = AUser.getUserById(travellerId);
        LocalDate start = LocalDate.parse(startTime);
        LocalDate end = LocalDate.parse(endTime);
        long days = ChronoUnit.DAYS.between(start, end);
        double unitPrice = 9999999;

        Order order = new Order();

        switch (priceType) {
            case "guider_price":
                unitPrice = Double.parseDouble(guider.guider_price);
                order.serviceType = "徒步向导旅行服务";
                break;
            case "guiderdrive_price":
                unitPrice = Double.parseDouble(guider.guiderdrive_price);
                order.serviceType = "五座车向导旅行服务";
                break;
            case "guiderpickup_price":
                unitPrice = Double.parseDouble(guider.guiderpickup_price);
                order.serviceType = "五座车接送机向导旅行服务";
                break;
        }
        double count = (num - 1) * 0.3 + 1;
        Double totalPrice = unitPrice * count * days;
        order.guider_id = guider.id;
        order.guider_name = guider.name;
        order.status = Order.CREATING;
        order.total_price = totalPrice;
        order.platform_fee = totalPrice * 0.1;
        order.traveller_id = traveller.id;
        order.traveller_name = traveller.name;
        order.policy = Order.POLICY_MIDIUM;
        order.startDate = startTime;
        order.endDate = endTime;
        order.days = days;
        order.travellerNum = num;
        order.travelPlace = guider.city_and_country;
        Order.save(order);
        Map<String, Object> result = new HashMap<>();
        result.put("guiderId", order.guider_id);
        result.put("orderId", order.id);
        return ok(Json.toJson(result));


    }

    public static Result confirmOrder() {
        Map<String, String[]> formData = request().body().asFormUrlEncoded();
        String orderId = formData.get("orderId")[0];
        String remark = formData.get("remark")[0];
        Order order = Order.get(orderId);
        order.remark = remark;
        Order.update(order);
        Map<String, Object> result = new HashMap<>();
        result.put("orderId", order.id);
        return ok(Json.toJson(result));
    }

    public static Result acceptOrder(String orderId) {
        Order order = Order.get(orderId);
        String userId = session("userId");
        if (userId.equals(order.guider_id)) {
            order.status = Order.INSERVICE;
            Order.update(order);
            //TODO :通知用户导游已接单
            return redirect("/setting/myorders");
        } else {
            return redirect("/logout");
        }
    }

    public static Result cancelOrder(String orderId) {
        Order order = Order.get(orderId);
        String userId = session("userId");
        if (userId.equals(order.guider_id)) {
            order.status = Order.CANCELED;
            Order.update(order);
            //TODO :退款
            //TODO :通知用户导游已取消订单
            return redirect("/setting/myorders");
        } else {
            return redirect("/logout");
        }
    }
}

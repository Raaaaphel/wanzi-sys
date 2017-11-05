package controllers;

import models.AUser;
import models.DemandOrder;
import models.GroupOrder;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import service.MailService;

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
        for (int i=0; i<areas.length; i++){
            bodyBuilder.append(areas[i] + "  ");
        }
        bodyBuilder.append("</p><p>出行人数：");
        bodyBuilder.append(order.getPersonNum());
        bodyBuilder.append("</p><p>是否需要用车：");
        if (order.getCar().equals(1)){
            bodyBuilder.append("是");
        } else {
            bodyBuilder.append("否");
        }
        bodyBuilder.append("</p><p>是否需要接送机：");
        if (order.getCar().equals(1)){
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


}

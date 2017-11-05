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
    public static Result demandPage(){
        AUser u = AUser.getUserById(session("userId")) ;
        if (u == null) {return RegisterController.onLogout("请先登录!");}

        return ok(views.html.demand.render());
    }

    /**
     * uri /intent/addOlister
     * 提交旅行计划
     */
    public static Result onDemand(){
        Map<String, String[]> formData = request().body().asFormUrlEncoded();

        DemandOrder order = new DemandOrder();
        order.setUserId(session("userId"));
        order.setStartTime(formData.get("start")[0]);
        order.setEndTime(formData.get("end")[0]);
        order.setTravelArea(formData.get("area"));
        order.setCar(Integer.parseInt(formData.get("car")[0]));
        order.setPick(Integer.parseInt(formData.get("pick")[0]));
        order.setContent(formData.get("content")[0]);
        DemandOrder.addDemandOrder(order);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 1000);
        return ok(Json.toJson(result));
    }

    /**
     * uri /site/group
     */
    public static Result groupDemand(){
        return ok(views.html.group.render());
    }

    /**
     * uri /intent/addFirm
     * 提交团队旅行计划，向工作邮箱中发送邮件
     */
    public static Result onGroupDemand(){
        MailService mailService = new MailService();
        Map<String, String[]> formData = request().body().asFormUrlEncoded();
        GroupOrder order = new GroupOrder();
        order.setContent(formData.get("content")[0]);
        order.setFirmName(formData.get("firm_name")[0]);
        order.setPhone(formData.get("phone")[0]);
        order.setUserName(formData.get("user_name")[0]);
        order.setUserEmail(formData.get("user_email")[0]);
        mailService.sendGroupDemandMail(order);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 1000);
        return ok(Json.toJson(result));
    }


}

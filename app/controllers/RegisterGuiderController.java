package controllers;


import play.libs.Json;
import play.mvc.*;

import models.*;

import java.util.*;

import play.data.*;
import play.Configuration;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class RegisterGuiderController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    static String homepage = Configuration.root().getString("webserverhost");

    /*
    @Inject
    final EbeanServer ebeanServer;

    public void createCustomer(String name) {

	Customer customer = new Customer();
	customer.setName(name);

	ebeanServer.save(customer);

    }
    */
    public static Result registerGuider() {
        AUser u = AUser.getUserById(session("userId"));
        if (u == null) return RegisterController.onLogout("请登录!");
        return ok(views.html.registerguider.render(u));
    }

    /**
     * 保存导游基本信息
     *
     * @return
     */
    public static Result onRegisterGuider() {

        Form<AUser> filledForm = Form.form(AUser.class).bindFromRequest();
        if (filledForm.hasErrors()) {
            return badRequest(views.html.registerguider.render(new AUser()));
        } else {
            Map<String, String> newData = filledForm.data();
            AUser u = AUser.getUserById(session().get("userId"));
            if (u == null) {
                return RegisterController.onLogout("请先登录!");
            }
            u.type = "GUIDER";
            u.city_and_country = newData.get("city_and_country");
            u.gender = newData.get("gender");
            u.birthplace = newData.get("au_home");
            u.wechat = newData.get("wechat");
            u.degree = newData.get("au_edu");
            u.type_work = newData.get("type_work");
            u.birth_day = newData.get("birth_day");
            u.email = newData.get("email");
            u.name = newData.get("name");

            if (u.type_work.equals(AUser.EMPLOYEE)) {
                u.jobtitle = newData.get("job");
            } else if (u.type_work.equals(AUser.STUDENT)) {
                u.major = newData.get("major");
            }
            AUser.update(u);
            return ok();
        }
    }

    public static Result applyPic() {
        AUser u = AUser.getUserById(session("userId"));
        if (u == null) return RegisterController.onLogout("请登录!");
        return ok(views.html.applyPic.render(u));
    }

    /**
     * 保存导游照片信息
     */
    public static Result onApplyPic() {
        Map<String, String[]> formData = request().body().asFormUrlEncoded();
        String face = formData.get("au_face")[0];
        String avatar = formData.get("au_avatar")[0];
        String identity = formData.get("au_identity")[0];
        String degree = formData.get("au_degree")[0];
        AUser u = AUser.getUserById(session().get("userId"));
        if (u == null) {
            return RegisterController.onLogout("请先登录!");
        }
        u.img_theme = "/public/upload/images/" + face;
        u.img_profile = "/public/upload/images/" + avatar;
        u.img_passport = "/public/upload/images/" + identity;
        u.img_degree = "/public/upload/images/" + degree;
        AUser.update(u);

        Map<String, Object> data = new HashMap<>();
        data.put("code", 1000);
        return ok(Json.toJson(data));
    }

    public static Result applyService() {
        AUser u = AUser.getUserById(session("userId"));
        if (u == null) return RegisterController.onLogout("请登录!");

        return ok(views.html.applysService.render());
    }

    /**
     * 保存导游简介信 息
     */
    public static Result onApplyService() {
        Map<String, String[]> formData = request().body().asFormUrlEncoded();
        AUser u = AUser.getUserById(session().get("userId"));
        if (u == null) {
            return RegisterController.onLogout("请先登录!");
        }
        String titel = formData.get("as_title")[0];
        String about_text = formData.get("as_about_text")[0];
        Integer aboutSize = Integer.valueOf(formData.get("as_about_mix_size")[0]);
        Integer introduceSize = Integer.valueOf(formData.get("as_introduce_mix_size")[0]);
        StringBuffer desc = new StringBuffer();
        desc.append("<h4 style=\"color:#333\">关于在这座城市的我</h4>");
        desc.append("<p style=\"font-size:14px;color:#666\">");
        desc.append(about_text);
        desc.append("</p>");
        List<String> aboutImgs = new ArrayList<>();
        for (int i = 0; i < aboutSize; i++) {
            String imgPath = "/public/upload/images/" + formData.get("as_about_mix[" + i + "][img]")[0];
            aboutImgs.add(imgPath);
            desc.append("<p><img src=\"");
            desc.append(imgPath);
            desc.append("\"  class=\"img-responsive img-thumbnail\"></p>");
            desc.append("<p style=\"font-size:14px;color:#666\">");
            desc.append(formData.get("as_about_mix[" + i + "][content]")[0]);
            desc.append("</p>");
        }
        desc.append("<h4 style=\"color:#333\">我眼中的这座城市</h4>");
        desc.append("<p style=\"font-size:14px;color:#666\">");
        desc.append(about_text);
        desc.append("</p>");
        List<String> introduceImgs = new ArrayList<>();
        for (int i = 0; i < introduceSize; i++) {
            String imgPath = "/public/upload/images/" + formData.get("as_introduce_mix[" + i + "][img]")[0];
            introduceImgs.add(imgPath);
            desc.append("<p><img src=\"");
            desc.append(imgPath);
            desc.append("\" class=\"img-responsive img-thumbnail\"></p>");
            desc.append("<p style=\"font-size:14px;color:#666\">");
            desc.append(formData.get("as_introduce_mix[" + i + "][content]")[0]);
            desc.append("</p>");
        }


        u.traveldisc = desc.toString();
        u.traveltitle = titel;
        u.imgs_introduce = introduceImgs;
        u.imgs_about = aboutImgs;
        AUser.update(u);

        return ok("{\"code\":1000}");
    }

    public static Result applyPrice() {
        AUser u = AUser.getUserById(session("userId"));
        if (u == null) return RegisterController.onLogout("请登录!");
        if (!u.type.equals(AUser.GUIDER)) return RegisterController.onLogout("请先注册导游！");
        return ok(views.html.applyprice.render(u));
    }

    public static Result onApplyPrice() {
        Map<String, String[]> formData = request().body().asFormUrlEncoded();
        AUser u = AUser.getUserById(session().get("userId"));
        if (u == null) {
            return RegisterController.onLogout("请先登录!");
        }
        String price = formData.get("guider_price")[0];
        String driver_price = formData.get("driver_price")[0];
        String pickup_price = formData.get("pickup_price")[0];

        u.guider_price = price;
        u.guiderdrive_price = driver_price;
        u.guiderpickup_price = pickup_price;
        AUser.update(u);
        String resultJson = "{\"code\":1000}";
        return ok(resultJson);
    }


}


package controllers;


import play.libs.Json;
import play.mvc.*;

import models.*;
import java.util.*;
import play.data.*;

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
    static String homepage = Application.homepage;

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
        String userId = session("userId");
        AUser u = AUser.getUserById(userId);
        if (u != null){
            return ok(views.html.registerguider.render());
        } else {
            return RegisterController.onLogout();
        }
    }

    /**
     * 保存导游基本信息
     * @return
     */
    public static Result onRegisterGuider() {

        Form<AUser> filledForm = Form.form(AUser.class).bindFromRequest();
        if (filledForm.hasErrors()) {
            return badRequest(views.html.registerguider.render());
        } else {
            Map<String, String> newData = filledForm.data();
            AUser u = AUser.getUserById(session().get("userId"));
            if (u == null){
                return RegisterController.onLogout();
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
                u.jobtitle = newData.get("major");
            }
            AUser.update(u);
            return ok(views.html.applyPic.render());
        }
    }

    public static Result applyPic(){
        return ok(views.html.applyPic.render());
    }

    /**
     * 保存导游照片信息
     * @return
     */
    public static Result onApplyPic(){
        Map<String, String[]> formData = request().body().asFormUrlEncoded();
        String face = formData.get("au_face")[0];
        String avatar = formData.get("au_avatar")[0];
        String identity = formData.get("au_identity")[0];
        String degree = formData.get("au_degree")[0];
        AUser u = AUser.getUserById(session().get("userId"));
        if (u == null){
            return RegisterController.onLogout();
        }
        u.img_theme = "/upload/images/" + face;
        u.img_profile = "/upload/images/" + avatar;
        u.img_passport = "/upload/images/" + identity;
        u.img_degree = "/upload/images/" + degree;
        AUser.update(u);

        Map<String, Object> data = new HashMap<>();
        data.put("code", 1000);
        return ok(Json.toJson(data));
    }

    public static Result applyService(){
        return ok(views.html.applysService.render());
    }

    /**
     * 保存导游简介信 息
     * @return
     */
    public static Result onApplyService(){
        Map<String, String[]> formData = request().body().asFormUrlEncoded();
        AUser u = AUser.getUserById(session().get("userId"));
        if (u == null) {
            return RegisterController.onLogout();
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
        for (int i = 0; i < aboutSize; i++){
            desc.append("<p><img src=\"/upload/images/" );
            desc.append(formData.get("as_about_mix["+i+"][img]")[0]);
            desc.append("\"  class=\"img-responsive img-thumbnail\"></p>");
            desc.append("<p style=\"font-size:14px;color:#666\">");
            desc.append(formData.get("as_about_mix["+i+"][content]")[0]);
            desc.append("</p>");
        }
        desc.append("<h4 style=\"color:#333\">我眼中的这座城市</h4>");
        desc.append("<p style=\"font-size:14px;color:#666\">");
        desc.append(about_text);
        desc.append("</p>");
        for (int i = 0; i < introduceSize; i++){
            desc.append("<p><img src=\"/upload/images/" );
            desc.append(formData.get("as_introduce_mix["+i+"][img]")[0]);
            desc.append("\" class=\"img-responsive img-thumbnail\"></p>");
            desc.append("<p style=\"font-size:14px;color:#666\">");
            desc.append(formData.get("as_introduce_mix["+i+"][content]")[0]);
            desc.append("</p>");
        }


        u.traveldisc = desc.toString();
        u.traveltitle = titel;
        AUser.update(u);

        return ok("{\"code\":1000}");
    }




}


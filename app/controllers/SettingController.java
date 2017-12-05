package controllers;


import play.mvc.*;

import models.*;

import java.util.*;


/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class SettingController extends Controller {
    public static Result getSetting() {
        return ok(views.html.setting.render());
    }

    public static Result getMyOrders() {
        String userId = session("userId");
        String userType = session("userType");
        List<Order> guider_orders = new ArrayList<>();
        List<Order> traveller_orders = Order.getCustomerOrders(userId);
        if (userType.equals(AUser.GUIDER)) {
            guider_orders = Order.getGuiderOrders(userId);
        }
        return ok(views.html.myorder.render(traveller_orders, guider_orders));
    }

    public static Result refer() {
        AUser guider = AUser.getUserById(session("userId"));
        return ok(views.html.refer.render(guider));
    }

}

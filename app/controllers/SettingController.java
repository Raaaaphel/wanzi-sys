package controllers;


import play.libs.Json;
import play.mvc.*;

import models.*;

import java.util.*;
import java.lang.*;
import play.data.*;
import play.Configuration;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class SettingController extends Controller {
	public static Result getSetting()
	{
		return ok(views.html.setting.render());
	}

	public static Result getMyOrders()
	{
		
		List<Order> traveller_orders = new ArrayList<Order>();
		List<Order> guider_orders = new ArrayList<Order>();
		
		return ok(views.html.myorder.render(traveller_orders, guider_orders));
	}
	public static Result refer()
	{
		return ok(views.html.refer.render());
	}
	
}

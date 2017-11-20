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
public class SettingController extends Controller {
	public static Result getSetting()
	{
		return ok(views.html.setting.render());
	}
}

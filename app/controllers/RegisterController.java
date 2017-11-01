package controllers;

import play.mvc.*;
/*import static play.api.data.Form.*;
import static play.data.Form.form;
*/
import models.*;

//import play.api.data.*;
import play.data.*;

import java.util.Map;
import play.Configuration;

public class RegisterController extends Controller {
	
	//	static String homepage = Application.homepage;
	static String homepage = Configuration.root().getString("webserverhost");

	/*
    public static Result index() {
	return redirect(routes.Application.tasks());
    }
	*/

	public static Result register()
	{    

	    Form<AUser> userForm = Form.form(AUser.class);
		return ok(		views.html.loginPage.t.register.render(userForm) );
	}
	
    public static Result onRegister() {
		
		Form<AUser> filledForm = Form.form(AUser.class).bindFromRequest();

		if(filledForm.hasErrors()) {
			return badRequest(views.html.loginPage.t.register.render(filledForm)); 
		}
		else{

			AUser u = new AUser();
			Map<String, String> formData = filledForm.data();
			u.name = formData.get("name");
			u.email = formData.get("email");
			u.password = formData.get("password");
			AUser.create(u);

			return 	redirect("/index.html");
		}
    }

	public static Result login()
	{

		return ok(views.html.loginPage.t.login.render(""));
	}

	public static Result onLogin()
	{
		Form<AUser> filledForm = Form.form(AUser.class).bindFromRequest();
		String em = filledForm.data().get("email");
		String pass = filledForm.data().get("password");
		//play.Logger.info(em);
		//play.Logger.info(pass);
		
		AUser u = AUser.verifyUser(em, pass) ;
				//		play.Logger.info(request().getQueryString("emmail"));
		if (em!=null && pass !=null && u != null)
			{
				session("username", u.name);
				session("userId",u.id);
				session("userType",u.type);
				return redirect("/index");
			}
		else{
			String alert = "请输入正确的用户名和密码";
			return ok (views.html.loginPage.t.login.render(alert));
		}
	}

	public static Result onLogout(String alert){
		session().remove("username");
		session().remove("userId");
		session().remove("userType");
		return ok(views.html.loginPage.t.login.render(alert));
	}
	/*
	public static Result onStep1 (){
		return ok(views.html.loginPage.t.2.render());
	}
	
	public static Result onStep2 (){
		redirect(homepage);
	}
	*/


}


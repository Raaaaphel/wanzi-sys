package controllers;

import play.mvc.Http.Context;
import play.mvc.*;
import play.data.*;

import models.*;
//import views.t.*;
import java.util.*;

public class Application extends Controller {
	//	static String staticpage = "http://static.zouzouyouyou.info/";
	//  static String homepage = "http://www.zouzouyouyou.info/";
	/*
	public static Result externalstatic(String path)
	{
		return redirect(staticpage + path);
	}
	*/
	static Form<Task> taskForm = Form.form(Task.class); 


	public static Result index_en(String refer){
		Context ctx = Context.current();
		//		ctx().changeLang("zh-cn");

		List<AUser> starGuiders = AUser.getStarGuiders(refer);
		
		return ok( views.html.index_en.render(starGuiders) );
	}
	
	public static Result index(String refer) {

		if (session("userId") == null){
			String newUserId = "999999999999999999999999";  //init session user id for new user
			session("userId", newUserId);
		}
		else{}

	    List<AUser> starGuiders = AUser.getStarGuiders(refer);
	    //      play.Logger.info(starGuiders.get(0).name);
	    return ok(  views.html.index.render(starGuiders));
	}
/*	public static Result index_en(String refer) {
	    //		return ok(	  views.html.index.render());

		if (session("userId") == null){
			String newUserId = "999999999999999999999999";  //init session user id for new user
			session("userId", newUserId);
		}
		else{}

	    List<AUser> starGuiders = AUser.getStarGuiders(refer);
	    //      play.Logger.info(starGuiders.get(0).name);
	    return ok(  views.html.index_en.render(starGuiders));
	}*/
  
  public static Result tasks() {
      return index(null);

  }

	/*
  public static Result newTask() {
    Form<Task> filledForm = taskForm.bindFromRequest();
      if(filledForm.hasErrors()) {
        return badRequest(
          views.html.index.render(Task.all(), filledForm)
        );
      } else {
        Task.create(filledForm.get());
        return redirect(routes.Application.tasks());  
      }
  }
	*/
  public static Result deleteTask(String id) {
    Task.delete(id);
    return redirect(routes.Application.tasks());
  }
  
}

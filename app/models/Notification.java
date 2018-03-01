package models;

import java.util.*;

import play.modules.mongodb.jackson.MongoDB;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.ObjectId;
import net.vz.mongodb.jackson.DBQuery;

public class Notification
{
	public Notification(){
		
	}
	
    public String title = "";
	public String content = "";
    public String link = "";
	public String sender_id = "";
	public String sender_name = "";
    public String receiver_id = "";
    public String receiver_name = "";

    public String timestamp = "";
    public String status = "";

    public static String UNREAD = "UNREAD";
    public static String READ = "READ";
}

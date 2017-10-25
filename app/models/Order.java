package models;

import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;
import play.modules.mongodb.jackson.MongoDB;

import java.util.ArrayList;
import java.util.List;

//@Entity
//@Table(name="order")
public class Order {
	public Order()
	{}

	@Id
	@ObjectId
	public String id;
	public String buyer_id;
	public String buyer_name;
	public String seller_id;
	public String seller_name;
	
	//	public HashMap serviceitems<String, int>; // (String service_name, int per_pirce, int quote, int item_price) // service_name=(GUIDER, CAR, PIACKUP)
	public int total_price;
	public String payment_timestamp; //DateTime
	public String status;
	public String comment;
	public String policy;

	public static String UNPAID = "UNPAID";
	public static String PAID = "PAID";
	public static String INSERVICE = "INSERVICE";
	public static String COMPLETE= "COMPLETE";
	public static String USERCONFIRMED= "USERCONFIRMED";

	public static String POLICY_STRICT = "STRICT";
	public static String POLICY_MIDIUM = "MIDIUM";
	public static String POLICY_LOOSE = "LOOSE";

	private static JacksonDBCollection<Order, String> coll = MongoDB.getCollection("order", Order.class, String.class);
	public static void save(Order order) {
		Order.coll.save(order);
	}

	public static Order get(String id)
	{
		return Order.coll.findOneById(id);
	}
}

package models;

import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;
import play.modules.mongodb.jackson.MongoDB;

import java.util.ArrayList;
import java.util.List;

//游客下单购买导游服务
public class Order {
    public Order()
    {}

    @Id
    @ObjectId
    public String id;
    public String traveller_id;  //游客id
    public String traveller_name;
    public String guider_id;  //
    public String guider_name;
	
    //	public HashMap serviceitems<String, int>; // (String service_name, int per_pirce, int quote, int item_price) // service_name=(GUIDER, CAR, PIACKUP)
                                                  // 服务名称（导游，带车导游，接送机），单价，服务数量（天数），小记＝单价＊天数
    public int total_price;  //服务总价
    public String payment_timestamp; //DateTime
    public String status;  //订单状态
    public String comment;  //暂时空置
    public String policy;  //页面上的三种取消政策


    /* 订单状态 */
	public static String UNPAID = "UNPAID";
	public static String PAID = "PAID";
	public static String INSERVICE = "INSERVICE";
	public static String COMPLETE= "COMPLETE";
	public static String USERCONFIRMED= "USERCONFIRMED";
    /* 取消政策 */
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

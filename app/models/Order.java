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

    //订单详情
    public String serviceType;
    public String startDate;
    public String endDate;
    public Integer travellerNum;
    public Long days;
    public String travelPlace;

    //	public HashMap serviceitems<String, int>; // (String service_name, int per_pirce, int quote, int item_price) // service_name=(GUIDER, CAR, PIACKUP)
                                                  // 服务名称（导游，带车导游，接送机），单价，服务数量（天数），小记＝单价＊天数
    public Double total_price;  //服务总价
    public String payment_timestamp; //DateTime
    public String status;  //订单状态
    public String comment;  //暂时空置
    public String policy;  //页面上的三种取消政策
    public String remark; //订单备注


    /* 订单状态 */
    public static String CREATING = "CREATING";
	public static String UNPAID = "UNPAID";
	public static String PAID = "PAID";
	public static String INSERVICE = "INSERVICE";
	public static String COMPLETE= "COMPLETE";
	public static String USERCONFIRMED= "USERCONFIRMED";
	public static String CANCELED = "CANCELED";
    /* 取消政策 */
	public static String POLICY_STRICT = "STRICT";
	public static String POLICY_MIDIUM = "MIDIUM";
	public static String POLICY_LOOSE = "LOOSE";

	private static JacksonDBCollection<Order, String> coll = MongoDB.getCollection("order", Order.class, String.class);
	public static void save(Order order) {
		order.id = Order.coll.save(order).getSavedId();
	}
	public static void update(Order order){
	    coll.updateById(order.id,order);
    }

	public static Order get(String id)
	{
		return Order.coll.findOneById(id);
	}

	public static Order getByCustomerAndGuider(String customerId, String guiderId){
	    return coll.findOne(DBQuery.and(DBQuery.is("traveller_id", customerId), DBQuery.is("guider_id", guiderId),DBQuery.is("status", CREATING)));
    }
    public static List<Order> getGuiderOrders(String guiderId){
	    return  coll.find(DBQuery.and(DBQuery.is("guider_id", guiderId),DBQuery.is("status", PAID))).toArray();
    }public static List<Order> getCustomerOrders(String travellerId){
	    return  coll.find(DBQuery.and(DBQuery.is("traveller_id", travellerId),DBQuery.is("status", PAID))).toArray();
    }
}

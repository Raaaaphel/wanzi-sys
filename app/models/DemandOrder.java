package models;

import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;
import play.modules.mongodb.jackson.MongoDB;

/**
 * 定制旅游需求
 */
public class DemandOrder {
    @Id
    @ObjectId
    private String id;
    private String userId; //用户Id
    private String content; //需求描述
    private String startTime; //出行日期-开始时间
    private String endTime; //出行日期-结束时间
    private String[] travelArea; //旅行地点
    private Integer personNum; //出行人数
    private Integer car; //是否需要用车 0-否，1-是
    private Integer pick; //是否需要接机 0-否，1-是

    private static JacksonDBCollection<DemandOrder, String> coll = MongoDB.getCollection("demand", DemandOrder.class, String.class);

    public static void addDemandOrder(DemandOrder order){
        coll.save(order);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String[] getTravelArea() {
        return travelArea;
    }

    public void setTravelArea(String[] travelArea) {
        this.travelArea = travelArea;
    }

    public Integer getPersonNum() {
        return personNum;
    }

    public void setPersonNum(Integer personNum) {
        this.personNum = personNum;
    }

    public Integer getCar() {
        return car;
    }

    public void setCar(Integer car) {
        this.car = car;
    }

    public Integer getPick() {
        return pick;
    }

    public void setPick(Integer pick) {
        this.pick = pick;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

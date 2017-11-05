package models;

import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;
import play.modules.mongodb.jackson.MongoDB;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;

/**
 * MongoDB 地区实体类
 */
public class AreaInfo implements Serializable {
    @Id
    @ObjectId
    private String id;
    private String areaCurrency; //地区货币
    private String areaEn; //地区英文简称
    private String areaId; //地区id;
    private String areaImage; //地区照片
    private String areaIndex = ""; //地区索引
    private String areaName; //地区中文
    private String areaPin; //地区名称汉语拼音
    private String areaPX; //经度
    private String areaPY; //纬度
    private String areaTimeZone; //地区时区
    private String areaLevel; //地区层级
    private String areaParent; //父级区域id

    private static JacksonDBCollection<AreaInfo,String> coll = MongoDB.getCollection("location", AreaInfo.class,String.class);

    public AreaInfo() {
    }

    public AreaInfo(String areaCurrency, String areaEn, String areaId, String areaImage, String areaIndex, String areaName, String areaPin, String areaPX, String areaPY, String areaTimeZone) {
        this.areaCurrency = areaCurrency;
        this.areaEn = areaEn;
        this.areaId = areaId;
        this.areaImage = areaImage;
        this.areaName = areaName;
        this.areaPin = areaPin;
        this.areaPX = areaPX;
        this.areaPY = areaPY;
        this.areaTimeZone = areaTimeZone;
        this.areaIndex = areaIndex;
    }

    public String getAreaCurrency() {
        return areaCurrency;
    }

    public void setAreaCurrency(String areaCurrency) {
        this.areaCurrency = areaCurrency;
    }

    public String getAreaEn() {
        return areaEn;
    }

    public void setAreaEn(String areaEn) {
        this.areaEn = areaEn;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaImage() {
        return areaImage;
    }

    public void setAreaImage(String areaImage) {
        this.areaImage = areaImage;
    }

    public String getAreaIndex() {
        return areaIndex;
    }

    public void setAreaIndex(String areaIndex) {
        this.areaIndex = areaIndex;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaPin() {
        return areaPin;
    }

    public void setAreaPin(String areaPin) {
        this.areaPin = areaPin;
    }

    public String getAreaPX() {
        return areaPX;
    }

    public void setAreaPX(String areaPX) {
        this.areaPX = areaPX;
    }

    public String getAreaPY() {
        return areaPY;
    }

    public void setAreaPY(String areaPY) {
        this.areaPY = areaPY;
    }

    public String getAreaTimeZone() {
        return areaTimeZone;
    }

    public void setAreaTimeZone(String areaTimeZone) {
        this.areaTimeZone = areaTimeZone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAreaParent() {
        return areaParent;
    }

    public void setAreaParent(String areaParent) {
        this.areaParent = areaParent;
    }
    public String getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(String areaLevel) {
        this.areaLevel = areaLevel;
    }

    public static List<AreaInfo> searchArea(String keyword){
        return coll.find(DBQuery.regex("areaIndex", Pattern.compile(".*" + keyword  + ".*",
                Pattern.CASE_INSENSITIVE))).toArray();

    }

    /**
     * 保存地域信息
     */
    public static void saveAreas(List<AreaInfo> areaList){
        for (AreaInfo node : areaList){
            AreaInfo existArea = coll.findOne(DBQuery.is("areaId", node.getAreaId()));
            if (existArea != null){
                if (existArea.getAreaIndex().length() < node.getAreaIndex().length()) {
                    coll.updateById(existArea.getId(), node);
                }
            } else {
                coll.save(node);
            }
        }
    }

    /**
     * 判断数据库中是否有数据
     * @return
     */
    public static boolean hasArea(){
        Boolean result = false;
        if (coll.getCount() > 0){
            result = true;
        }
        return result;
    }

    public static List<AreaInfo> findAreaByParentId(String parentId){
       return coll.find(DBQuery.is("areaParent", parentId)).toArray();
    }
    public static AreaInfo getAreaById(String areaId){
        return coll.findOne(DBQuery.is("areaId", areaId));
    }
}

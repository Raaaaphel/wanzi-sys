package models;

import java.util.ArrayList;
import java.util.List;

public class AreaVo {
    private String areaId;
    private String areaName;
    private String areaImage;
    private Integer guiderCount;
    private List<AreaVo> childArea;
    private String currentArea;


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

    public Integer getGuiderCount() {
        return guiderCount;
    }

    public void setGuiderCount(Integer guiderCount) {
        this.guiderCount = guiderCount;
    }

    public List<AreaVo> getChildArea() {
        return childArea;
    }

    public void setChildArea(List<AreaInfo> areas) {
        List<AreaVo> areaVos = new ArrayList<>();
        for(AreaInfo info : areas){
            AreaVo temp = new AreaVo();
            temp.setAreaId(info.getAreaId());
            temp.setAreaName(info.getAreaName());
            areaVos.add(temp);
        }
        this.childArea = areaVos;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCurrentArea() {
        return currentArea;
    }

    public void setCurrentArea(String currentArea) {
        this.currentArea = currentArea;
    }
}

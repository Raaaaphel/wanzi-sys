package controllers;

import play.mvc.*;
import play.*;
//import play.Environment;
import play.libs.*;

import models.*;

import java.util.*;

//import org.codehaus.jackson.JsonFactory;
//import com.typesafe.config.ConfigFactory;
//import com.typesafe.config.Environment;
import java.io.*;
//import org.json.*;
import java.io.IOException;
//import org.codehaus.jackson.JsonNode;


public class SearchController extends Controller {

    public static Result onSearch(String key) {
        return ok(views.html.search.render(key));
    }

    public static Result getSearch() {
        //		Environment env = System.getProperty("environment");
        try (InputStream is = Play.application().resourceAsStream("/app/statics/js/getSearch.json")) {
                /*
                JsonFactory factory = new JsonFactory();
				JsonParser jp = factory.createJsonParser(is);
								JsonNode json = jp.readValueAsTree();
				

				ObjectMapper mapper = new ObjectMapper();
				JsonParser jp = new JsonParser(is);
								JsonNode json = mapper.readTree(jp);
								is.close();
								//				final JsonNode json = Json.parse(is);
								*/
            return ok(views.html.getSearch.render());
        } catch (IOException e) {
            e.printStackTrace();
            return internalServerError("java getSesarch(): getsearch.json error");

        }
    }


    public static Result getGuiderDetail(String userId) {
        AUser guider = AUser.getUserById(userId);
        if (guider == null) {
            guider = new AUser();
            guider.name = "导游姓名";
            guider.city_and_country = "国家-城市";
        }
        return ok(views.html.detail.render(guider));
    }

    //	public static Result


    public static Result setLocation() {
        return ok("ok");
    }

    public static Result searchArea() {
        String keyword = request().body().asFormUrlEncoded().get("keyword")[0];
        List<AreaInfo> areaList = AreaInfo.searchArea(keyword);
        return ok(Json.toJson(areaList));
    }

    public static Result findChildArea() {
        Map<String, String[]> formData = request().body().asFormUrlEncoded();
        String parentId = formData.get("parentId")[0];
        List<AreaInfo> areaList = AreaInfo.findAreaByParentId(parentId);
        return ok(Json.toJson(areaList));
    }

    public static Result getCountry(String cid) {
        AreaInfo area = AreaInfo.getAreaById(cid);
        AreaVo areaVo = new AreaVo();
        String[] exception = {"1", "2", "3", "4", "5", "6"};

        if (area.getAreaParent() == null || Arrays.asList(exception).contains(area.getAreaParent())) {
            List<AreaInfo> areaInfos = AreaInfo.findAreaByParentId(cid);
            areaVo.setChildArea(areaInfos);
            areaVo.setAreaId(cid);
            areaVo.setAreaName(area.getAreaName());
            areaVo.setAreaImage(area.getAreaImage());
            areaVo.setGuiderCount(AUser.searchGuider(area.getAreaName(), 0, 0).size());
        } else {
            AreaInfo pArea = AreaInfo.getAreaById(area.getAreaParent());
            List<AreaInfo> areaInfos = AreaInfo.findAreaByParentId(pArea.getAreaId());
            areaVo.setChildArea(areaInfos);
            areaVo.setAreaId(pArea.getAreaId());
            areaVo.setAreaName(pArea.getAreaName());
            areaVo.setAreaImage(pArea.getAreaImage());
            areaVo.setGuiderCount(AUser.searchGuider(pArea.getAreaName(), 0, 0).size());
        }
        areaVo.setCurrentArea(cid);
        return ok(views.html.getcountry.render(areaVo));
    }

    public static Result searchGuiders(){
        Map<String, String[]> formData = request().body().asFormUrlEncoded();
        String id = formData.get("id")[0];
        String page = formData.get("p")[0];
        String limit = formData.get("limit")[0];
        AreaInfo area = AreaInfo.getAreaById(id);
        List<AUser> guiders = AUser.searchGuider(area.getAreaName(), Integer.parseInt(limit), Integer.parseInt(page));
        Map<String, Object> result = new HashMap<>();
        if (guiders.size() > 0) {
            result.put("code", 1000);
            result.put("result", guiders);
        } else if (Integer.parseInt(page) > 1){
            result.put("code", 1001); //数据已加载完
        } else {
            result.put("code", 1005); //没有导游
        }
        return ok(Json.toJson(result));
    }

}

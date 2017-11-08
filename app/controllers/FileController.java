package controllers;

import play.Configuration;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData.FilePart;
import service.FileService;
import util.ImageUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileController extends Controller {
    public static String imgDir = Configuration.root().getString("imgUploadPath");


    /**
     * 处理未裁剪图片 ajaxFileUpload
     * @return
     */
    public static Result onUploadImage(String imgLable){
        FileService fileService = new FileService();
        List<FilePart> files = request().body().asMultipartFormData().getFiles();
        if (files != null && files.size() > 0) {
            File img = files.get(0).getFile();
            String oldName = files.get(0).getFilename();
            StringBuilder fileName = new StringBuilder(session("userId"));
            fileName.append("."+imgLable);
            String newFileName = "/public/upload/images/" + fileService.saveImage(img, oldName, fileName.toString(), imgDir);
            Map<String, Object> data = new HashMap<>();
            data.put("name", newFileName);
            data.put("code", 1000);
            return ok(Json.toJson(data));
        } else {
            return badRequest();
        }
    }

    public static Result onUploadThumbImage(){
        Map<String, String[]> formData = request().body().asFormUrlEncoded();
        String oldName = formData.get("img")[0];
        StringBuilder newName = new StringBuilder(oldName);
        String x = formData.get("x")[0];
        String y = formData.get("y")[0];
        String w = formData.get("w")[0];
        String h = formData.get("h")[0];
        Map<String, Object> data = new HashMap<>();
        if (!(w.equals("0") && h.equals("0"))) {
            newName.insert(oldName.lastIndexOf("."), ".cut");
            ImageUtil.cutPic(imgDir + oldName, imgDir + newName.toString(), Double.valueOf(x).intValue(), Double.valueOf(y).intValue(), Double.valueOf(w).intValue(), Double.valueOf(h).intValue());
            data.put("name", newName.toString());
            data.put("code", 1000);
        } else {
            data.put("name", oldName);
            data.put("code", 1000);
        }
        return ok(Json.toJson(data));
    }



}

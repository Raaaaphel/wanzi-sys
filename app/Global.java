

import models.AreaInfo;
import play.*;
import play.Application;
import service.FileService;
import util.LocationLoader;

public class Global extends GlobalSettings {

	@Override
	public void onStart(Application app) {
		super.onStart(app);
		//加载地区数据
		if (!AreaInfo.hasArea()){
			LocationLoader loader = new LocationLoader();
			loader.loadJson();
		}
		//创建上传图片目录
		FileService.mkdir(Configuration.root().getString("imgUploadPath"));
	}

	    
}

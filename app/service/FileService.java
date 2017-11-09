package service;

import play.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import play.Logger;

public class FileService {

    public String saveImage(File img, String oldName, String newFileName, String dir){
        String type = oldName.substring(oldName.lastIndexOf("."));
        Path tempFile = Paths.get(img.toURI());
        newFileName += type;
        Path toFile = Paths.get(dir, newFileName);
        try {
            Files.copy(tempFile, toFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            Logger.error("Image upload fail!");
        }
        return newFileName;
    }

    public static void mkdir(String path){
        File file = new File(path);
        if (!file.exists()){
            Logger.info("Make Dir " + path);
            if (!file.mkdirs()){
                Logger.error("Make Dir fail!");
            }
        }
    }
}

package util;

public class CheckClient {
    private static String mobileArr[] = {"iphone","ipad","andorid"};
    public static boolean isMobileClient(String clientType){
        for (String match : mobileArr){
            if (clientType.contains(match))
                return true;
        }
        return false;
    }
}

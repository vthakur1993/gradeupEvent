package con.example.interview.kotlintesting;

import android.os.Bundle;

import java.util.Map;

public class AppAnalytics implements AppAnalyticsConstants {
    private static final AppAnalytics ourInstance = new AppAnalytics();

    public static AppAnalytics getInstance() {
        return ourInstance;
    }


    private AppAnalytics() {
    }

    public void logEvent(String eventType, String id, String name, String content_type) {
        Bundle bundle = new Bundle();


    }

    public void logCustomEvent(String eventType, Map<String, String> eventMap) {
        Bundle bundle = new Bundle();
        for (Map.Entry<String, String> map : eventMap.entrySet()) {
            bundle.putString(map.getKey(), map.getValue());
        }

    }
    public UserProperties getUserProperty(){
        return  new UserProperties.UserPropertiesBuilder("manish", "english").build();
    }

    public DeviceInfo getDeviceInfo(){
        return  new DeviceInfo.DeviceInfoBuilder("Mobile", "Android").build();
    }

}
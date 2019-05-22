package con.example.interview.kotlintesting;

public class DeviceInfo {

     /*"category": "mobile",
             "mobile_brand_name": "Xiaomi",
             "mobile_model_name": "Redmi Note 5 Pro",
             "mobile_marketing_name": "",
             "mobile_os_hardware_model": "Redmi Note 5 Pro",
             "operating_system": "ANDROID",
             "operating_system_version": "8.1.0",
             "language": "en-in",
             "browser": "",
             "browser_version": ""*/

    private String category;
    private String operating_system;
    private String mobile_brand_name;    //optional
    private String mobile_marketing_name;   // optional
    private String operating_system_version;   // optional
    private String language;   // optional
    private String browser;   // optional
    private String browser_version;   // optional

    public void setCategory(String category) {
        this.category = category;
    }

    public void setOperating_system(String operating_system) {
        this.operating_system = operating_system;
    }

    public void setMobile_brand_name(String mobile_brand_name) {
        this.mobile_brand_name = mobile_brand_name;
    }

    public void setMobile_marketing_name(String mobile_marketing_name) {
        this.mobile_marketing_name = mobile_marketing_name;
    }

    public void setOperating_system_version(String operating_system_version) {
        this.operating_system_version = operating_system_version;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public void setBrowser_version(String browser_version) {
        this.browser_version = browser_version;
    }

    public static class DeviceInfoBuilder {
        private String category;
        private String operating_system;
        private String mobile_brand_name;    //optional
        private String mobile_marketing_name;   // optional
        private String operating_system_version;   // optional
        private String language;   // optional
        private String browser;   // optional
        private String browser_version;   // optional

        public DeviceInfoBuilder(String category, String operating_system) {
            this.category = category;
            this.operating_system = operating_system;

        }

        public DeviceInfo.DeviceInfoBuilder setMobileBrand(String mobile_brand_name) {
            this.mobile_brand_name = mobile_brand_name;
            return this;
        }

        public DeviceInfo build() {
            return new DeviceInfo(this);
        }
    }
    private DeviceInfo(DeviceInfo.DeviceInfoBuilder builder) {
        this.category = builder.category;
        this.operating_system = builder.operating_system;

    }


}

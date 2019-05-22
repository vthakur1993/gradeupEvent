package con.example.interview.kotlintesting;

public class UserProperties {

    private String user_id;
    private String language;
    private String phone;    //optional
    private String mail;   // optional

    public String getUser_id() {
        return user_id;
    }

    public String getLanguage() {
        return language;
    }

    public String getPhone() {
        return phone;
    }

    public String getMail() {
        return mail;
    }

    public static class UserPropertiesBuilder {
        private final String user_id;    //required
        private final String language;    //required

        private String phone;    //optional
        private String mail;    //optional

        public UserPropertiesBuilder(String user_id, String language) {
            this.user_id = user_id;
            this.language = language;

        }

        public UserPropertiesBuilder setPhone(String phone) {
            this.phone = phone;
            return this;
        }
        public UserPropertiesBuilder setMail(String mail) {
            this.mail = mail;
            return this;
        }
        public UserProperties build() {
            // call the private constructor in the outer class
            return new UserProperties(this);
        }
    }
    private UserProperties(UserPropertiesBuilder builder) {
        this.user_id = builder.user_id;
        this.language = builder.language;
        this.phone= builder.phone;
        this.mail = builder.mail;
    }

}
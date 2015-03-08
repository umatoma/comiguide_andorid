package net.umatoma.comiguide.util;

public class SharedPrefKeys {
    private SharedPrefKeys() {};

    public static final class User {
        public static final String PREF_NAME = "user";

        public static final String API_TOKEN = "api_token";
        public static final String USER_ID   = "user_id";
        public static final String USER_NAME = "user_name";
    }

    public static final class History {
        public static final String PREF_NAME =" history";

        public static final String COMIKET_CIRCLE_DAY = "comiket_circle_day";
        public static final String COMIKET_CIRCLE_MAP_ID = "comiket_circle_map_id";
    }
}

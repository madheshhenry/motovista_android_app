package com.example.splashactivity;

import android.provider.BaseColumns;

public final class ProfileContract {
    private ProfileContract() {}

    public static class ProfileEntry implements BaseColumns {
        public static final String TABLE_NAME = "customer_profile";
        public static final String COLUMN_NAME_FULLNAME = "fullname";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_PROFILE_URI = "profile_uri";
        public static final String COLUMN_NAME_AADHAAR_FRONT = "aadhaar_front";
        public static final String COLUMN_NAME_AADHAAR_BACK = "aadhaar_back";
        public static final String COLUMN_NAME_PAN = "pan";
        public static final String COLUMN_NAME_EMI_NOTIF = "emi_notif";
        public static final String COLUMN_NAME_REG_NOTIF = "reg_notif";
        public static final String COLUMN_NAME_DELIVERY_NOTIF = "delivery_notif";
    }
}

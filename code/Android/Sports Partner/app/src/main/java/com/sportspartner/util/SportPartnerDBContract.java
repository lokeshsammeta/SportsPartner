package com.sportspartner.util;

import android.provider.BaseColumns;

/**
 * @author Xiaochen Li
 */

public final class SportPartnerDBContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private SportPartnerDBContract() {}

    /* Inner class that defines the table contents */
    public static class LoginDB implements BaseColumns {
        public static final String TABLE_NAME = "login";
        public static final String COLUMN_EMAIL_NAME = "email";
        public static final String COLUMN_KEY_NAME = "key";
        public static final String COLUMN_REGISTRATIONID_NAME = "registerId";
    }

    /* Inner class that defines the table contents */
    public static class NotificationDB implements BaseColumns {
        public static final String TABLE_NAME = "notification";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE_NAME = "title";
        public static final String COLUMN_DETAIL_NAME = "detail";
        public static final String COLUMN_SENDER_NAME = "sender";
        public static final String COLUMN_TYPE_NAME = "type";
        public static final String COLUMN_TIME_NAME = "time";
        public static final String COLUMN_PRIORITY_NAME = "priority";

    }
}

package com.example.notepad.bullsandcows.data.databases.models;

public class UserInfoDB {

    public static final String TABLE = "UserInfo";

    public static final String ID = "_id";

    public static final String COUNTRY = "country";

    public static final String EMAIL = "email";

    public static final String SEX = "sex";

    public static final String AGE = "age";

    public static final String AVATAR_URL = "avatar";

    public static final String DESCRIPTION = "description";

    public static final String COUNTRY_FLAG = "countryFlag";

    public static final String NUMBER_OF_PLAYED_GAMES = "playedGames";

    public static final String REGISTRATION_TIME = "registrationTime";

    public static final String LAST_VISIT = "lastVisit";

    public static final String IS_ONLINE = "online";

    public static final String USER_FRIENDS = "userFrands";

    public static final String USER_MESSAGES = "userMessages";

    public static final String USERS_BEST_RECORDS = "bestRecords";

    public static final String USER_LAST_RECORDS = "lastRecords";

    public static final String[] AVAILABLE_COLUMNS = {ID, COUNTRY, COUNTRY_FLAG, EMAIL, SEX, AGE, AVATAR_URL,
            DESCRIPTION, NUMBER_OF_PLAYED_GAMES, REGISTRATION_TIME, LAST_VISIT, IS_ONLINE, USER_LAST_RECORDS,
            USER_FRIENDS, USERS_BEST_RECORDS, USER_MESSAGES};
}

package edu.cmu.sv.ws.ssnoc.data;

/**
 * This class contains all the SQL related code that is used by the project.
 * Note that queries are grouped by their purpose and table associations for
 * easy maintenance.
 *
 */
public class SQL {
    /*
     * List the USERS table name, and list all queries related to this table
     * here.
     */
    public static String SSN_USERS = "SSN_USERS";
    public static String SSN_MESSAGES = "SSN_MESSAGES";
    public static String PRIVATE_MESSAGES = "PRIVATE_MESSAGES";
    public static final String TEST_POST = "TEST_POST";
    public static final String TEST_GET = "TEST_GET";
    public static final String TEST_RESULTS = "TEST_RESULTS";
    public static final String MEMORY_TEST = "MEMORY_TEST";
    public static String SSN_ANNOUNCEMENTS = "SSN_ANNOUNCEMENTS";


    /**
     * Query to check if a given table exists in the H2 database.
     */
    public static final String CHECK_TABLE_EXISTS_IN_DB = "SELECT count(1) as rowCount "
            + " FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = SCHEMA() "
            + " AND UPPER(TABLE_NAME) = UPPER(?)";


    public static final String CREATE_USERS = "create table IF NOT EXISTS " + SSN_USERS +
            "(user_id IDENTITY PRIMARY KEY," +
            "user_name VARCHAR(255) NOT NULL," +
            "password varchar(40) NOT NULL,"+
            "emergency_status smallint ,"+
            "role smallint,"+
            "active boolean," +
            "salt VARCHAR(512)  );";

    public static final String CREATE_MESSAGES = "CREATE TABLE IF NOT EXISTS "+ SSN_MESSAGES +" (" +
            "pid integer NOT NULL AUTO_INCREMENT,"+
            "message varchar(255),"+
            "author varchar(255),"+
            "timestamp timestamp,"+
            "PRIMARY KEY (pid),"+
            "FOREIGN KEY (author) REFERENCES public.SSN_USERS(user_name)"+
            ");";

    public static final String CREATE_PM = "CREATE TABLE IF NOT EXISTS " + PRIVATE_MESSAGES + " (" +
            "content varchar(512)," +
            "author varchar(255)," +
            "target varchar(255)," +
            "postedAt timestamp," +
            "messageId smallint NOT NULL AUTO_INCREMENT PRIMARY KEY);";

    public static final String CREATE_TEST_POST = "CREATE TABLE IF NOT EXISTS "+ TEST_POST +" (" +
            "pid integer NOT NULL AUTO_INCREMENT,"+
            "message varchar(255),"+
            "author varchar(255),"+
            "timestamp timestamp,"+
            "PRIMARY KEY (pid),"+
            ");";

    public static final String CREATE_TEST_GET = "CREATE TABLE IF NOT EXISTS "+ TEST_GET +" (" +
            "pid integer NOT NULL AUTO_INCREMENT,"+
            "message varchar(255),"+
            "author varchar(255),"+
            "timestamp timestamp,"+
            "PRIMARY KEY (pid),"+
            ");";

    public static final String CREATE_TEST_RESULTS = "CREATE TABLE IF NOT EXISTS " + TEST_RESULTS + " (" +
            "number_of_posts integer," +
            "number_of_gets integer," +
            "timestamp timestamp," +
            "testTime integer);";

    public static final String CREATE_MEMORY_TEST = "CREATE TABLE IF NOT EXISTS " + MEMORY_TEST + " (" +
            "timestamp timestamp," +
            "volatile_used long," +
            "volatile_left long," +
            "non_volatile_used long," +
            "non_volatile_left long);";

    public static final String CREATE_ANNOUNCENMENTS = "CREATE TABLE IF NOT EXISTS "+ SSN_ANNOUNCEMENTS +" (" +
            "pid integer NOT NULL AUTO_INCREMENT,"+
            "message varchar(255),"+
            "author varchar(255),"+
            "timestamp timestamp,"+
            "PRIMARY KEY (pid),"+
            "FOREIGN KEY (author) REFERENCES public.SSN_USERS(user_name)"+
            ");";

    public static final String UPDATE_MEMORY = "insert into " + MEMORY_TEST + "(timestamp, volatile_used, volatile_left, non_volatile_used, non_volatile_left)" +
            " values (?, ?, ?, ?, ?)";

    public static final String GET_MEMORY_RESULTS = "select * from " + MEMORY_TEST;

    public static final String RESET_MEMORY = "delete from " + MEMORY_TEST;

    public static final String INSERT_INTO_TEST1 = "insert into " + TEST_GET + " (message, author, timestamp) values ('hello this is a message', 'TESTAUTH', '2014-10-13 05:52:11')";
    public static final String INSERT_INTO_TEST2 = "insert into " + TEST_GET + " (message, author, timestamp) values ('ni hao zhe shi yi ge message', 'TESTAUTH2', '2014-10-13 05:52:11')";
    public static final String INSERT_INTO_TEST3 = "insert into " + TEST_GET + " (message, author, timestamp) values ('konnichiwa kore wa message dearu', 'TESTAUTH3', '2014-10-13 05:52:11')";
    public static final String INSERT_TIME_INTO_TEST = "insert into " + TEST_RESULTS + " (timestamp, testTime, number_of_posts, number_of_gets) values (?, ?, 0, 0)";

    public static final String GET_TEST_RESULTS = "select number_of_posts, number_of_gets, testTime from " + TEST_RESULTS;
    public static final String DROP_TEST_GET = "DROP TABLE " + TEST_GET;
    public static final String DROP_TEST_POST = "DROP TABLE " + TEST_POST;
    public static final String DROP_TEST_RESULTS = "DROP TABLE " + TEST_RESULTS;

    public static final String TEST_POST_ON_WALL = "insert into " + TEST_POST +
            "(message, author, timestamp) values (?,?,?)";

    public static final String TEST_GET_FROM_WALL = "select * from " + TEST_GET;

    public static final String TEST_COUNT_POST = "update " + TEST_RESULTS + " SET number_of_posts = number_of_posts + 1";
    public static final String TEST_COUNT_GET = "update " + TEST_RESULTS + " SET number_of_gets = number_of_gets + 1";

    public static final String TEST_CHECK_TIME = "select NOW() < timestamp FROM " + TEST_RESULTS;
    /**
     * Query to load all users in the system.
     */
    public static final String FIND_ALL_USERS = "select user_id, user_name, password,"
            + " emergency_status," + " salt, role " + " from " + SSN_USERS + " where active=TRUE order by user_name ";

    /**
     * Query to find a user details depending on his name. Note that this query
     * does a case insensitive search with the user name.
     */
    public static final String FIND_USER_BY_NAME = "select user_id, user_name, password,"
            + " emergency_status,"
            + " salt, role, active "
            + " from "
            + SSN_USERS
            + " where UPPER(user_name) = UPPER(?)";

    public static final String GET_ALL_PUBLIC_MESSAGES = "select * from " + SSN_MESSAGES;

    public static final String SEND_PRIVATE_MESSAGE = "insert into " + PRIVATE_MESSAGES +
            " (content, author, target, postedAt) values (?, ?, ?, ?)";

    public static final String GET_PM_BY_USER_ID = "select * from " + PRIVATE_MESSAGES +
            " where (author = ? and target = ?) or (author = ? and target = ?) ORDER BY postedAt ASC";

    public static final String GET_CHAT_BUDDIES = "select target from " + PRIVATE_MESSAGES +
            " where author = ?";

    public static final String GET_CHAT_BUDDIES2 = "select author from " + PRIVATE_MESSAGES +
            " where target = ?";

    public static final String GET_MESSAGE_BY_ID = "select content from " + PRIVATE_MESSAGES +
            " where messageId = ?";

    public static final String POST_ON_WALL = "insert into " + SSN_MESSAGES +
            "(message, author, timestamp) values (?,?,?)";

    public static final String FIND_STATUS_BY_NAME = "select emergency_status from SSN_USERS where UPPER(user_name) = UPPER(?)";

    public static final String FIND_TALKERS_BY_TIME = "select author, target from " + PRIVATE_MESSAGES +
            " where postedAt > ?";

    public static final String FIND_ALL_USERNAMES = "select user_name from " + SSN_USERS + " where active=TRUE";


    public static final String POST_ANNOUNCEMENT = "insert into " + SSN_ANNOUNCEMENTS +
            "(message, author, timestamp) values (?,?,?)";

    public static final String GET_ALL_ANNOUNCEMENTS = "select * from " + SSN_ANNOUNCEMENTS;

    /**
     * Query to insert a row into the users table.
     */
    public static final String INSERT_USER = "insert into " + SSN_USERS
            + " (user_name, password, emergency_status, salt, role, active) values (?, ?, ?, ?, 0, TRUE)";

    public static final String INSERT_ADMIN = "insert into " + SSN_USERS
            + " (user_name, password, emergency_status, salt, role, active) values ('SSNAdmin', 'd9289f510850320b7c41ea10547d2ab8', 3, '37b8e9a65cfed2766af2d11879c86e82', 3, TRUE)";

    public static final String UPDATE_STATUS = "UPDATE " + SSN_USERS + " SET emergency_status=? WHERE user_name=?";

    public static final String UPDATE_USERNAME = "UPDATE " + SSN_USERS + " SET user_name=? WHERE user_name=?";
    public static final String UPDATE_PASSWORD = "UPDATE " + SSN_USERS + " SET password=?, salt=? WHERE user_name=?";
    public static final String UPDATE_ROLE = "UPDATE " + SSN_USERS + " SET role=? WHERE user_name=?";
    public static final String UPDATE_ACTIVE = "UPDATE " + SSN_USERS + " SET active=? WHERE user_name=?";

    public static final String SEARCH_USERNAME = "SELECT user_id, user_name, password, emergency_status, salt, role from " + SSN_USERS + " WHERE user_name LIKE ?";
    public static final String SEARCH_STATUS = "SELECT user_id, user_name, password, emergency_status, salt, role from " + SSN_USERS + " WHERE emergency_status=?";
    public static final String SEARCH_WALL = "SELECT * from " + SSN_MESSAGES + " WHERE message LIKE ?";
    public static final String SEARCH_ANNOUNCEMENTS = "SELECT * from " + SSN_ANNOUNCEMENTS + " WHERE message LIKE ?";
    public static final String SEARCH_PM = "SELECT * from " + PRIVATE_MESSAGES + " WHERE content LIKE ? AND (author=? OR target=?)";

    public static final String DOES_USER_EXIST = "SELECT count(1) from " + SSN_USERS + " WHERE user_name = ?";
}

package ubaidgul.appdeveloper.auqaf.DataSender;

public class Constants {

    // Server URL
//    public static final String URL_SURVEY_DATA = "http://192.168.52.1:8080/tree_plantation_punjab_2019/survey_data_tree_plantation";

//    public static final String URL_SURVEY_DATA = "http://202.166.167.123:8082/road_demarcation/road_demarcation_data";
    public static final String URL_SURVEY_DATA = "http://192.168.43.127:8080/road_demarcation/road_demarcation_data";
//    public static final String URL_LOGIN = "http://192.168.43.127:8080/road_demarcation/app_login";
    public static final String URL_LOGIN = "http://202.166.167.123:8082/road_demarcation/app_login";
//    public static final String URL_REGISTRATION = "http://192.168.43.127:8080/road_demarcation/sign_up";
    public static final String URL_REGISTRATION = "http://202.166.167.123:8082/road_demarcation/sign_up";
    public static final String URL_Menu_Update_App = "http://202.166.167.123:8082/road_demarcation/getAppVersion?version=";
    public static final String URL_DATA_SYNC = "http://202.166.167.123:8082/road_demarcation/plant_types_data";
    public static final String URL_VALIDATE_RECEIVER_FOR_DONATION = "http://202.166.167.123:8082/road_demarcation/validate_receiver";

    //	add table names and URLs for other activities
    public static final String TABLE_SURVEY_DATA_INDUSTRY = "tbl_survey_data_ind";
    public static final String TABLE_PLANT_TYPES_DATA = "plant_types";


    public static final int LOCATION_SERVICE_ID = 175;
    public static final String ACTION_START_LOCATION_SERVICE = "startLocationService";
    public static final String ACTION_STOP_LOCATION_SERVICE = "stopLocationService";
}

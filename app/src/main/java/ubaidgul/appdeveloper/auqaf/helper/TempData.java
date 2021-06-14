package ubaidgul.appdeveloper.auqaf.helper;

public class TempData {

	private static String imei = "";
	private static String localId = "";
	private static String systemDate = "";
	private static String latitude = "0";
	private static String longitude = "0";
	private static String imagePath = "";
	private static String imageName = "";
	private static String mobileDateTime = "";

	private static String imagePath2 = "";
	private static String imageName2 = "";
	private static String latitude2 = "0";
	private static String longitude2 = "0";

	public static void setLatitude2(String latitude2) {
		TempData.latitude2 = latitude2;
	}

	public static void setLongitude2(String longitude2) {
		TempData.longitude2 = longitude2;
	}

	public static String getLatitude2() {
		return latitude2;
	}

	public static String getLongitude2() {
		return longitude2;
	}


	public static String getImei() {
		return imei;
	}

	public static void setImei(String imei) {
		TempData.imei = imei;
	}

	public static String getLocalId() {
		return localId;
	}

	public static void setLocalId(String localId) {
		TempData.localId = localId;
	}

	public static String getSystemDate() {
		return systemDate;
	}

	public static void setSystemDate(String systemDate) {
		TempData.systemDate = systemDate;
	}

	public static String getLatitude() {
		return latitude;
	}

	public static void setLatitude(String latitude) {
		TempData.latitude = latitude;
	}

	public static String getLongitude() {
		return longitude;
	}

	public static void setLongitude(String longitude) {
		TempData.longitude = longitude;
	}

	public static String getImagePath() {
		return imagePath;
	}

	public static void setImagePath(String imagePath) {
		TempData.imagePath = imagePath;
	}

	public static String getImageName() {
		return imageName;
	}

	public static void setImageName(String imageName) {
		TempData.imageName = imageName;
	}

	public static String getMobileDateTime() {
		return mobileDateTime;
	}

	public static void setMobileDateTime(String mobileDateTime) {
		TempData.mobileDateTime = mobileDateTime;
	}

	public static String getImagePath2() {
		return imagePath2;
	}

	public static void setImagePath2(String imagePath2) {
		TempData.imagePath2 = imagePath2;
	}

	public static String getImageName2() {
		return imageName2;
	}

	public static void setImageName2(String imageName2) {
		TempData.imageName2 = imageName2;
	}
}

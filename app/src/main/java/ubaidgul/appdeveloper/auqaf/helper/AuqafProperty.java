
package ubaidgul.appdeveloper.auqaf.helper;

import java.io.Serializable;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuqafProperty implements Serializable, Parcelable
{

    @SerializedName("zone")
    @Expose
    private String zone;
    @SerializedName("circle")
    @Expose
    private String circle;
    @SerializedName("waqf_property_name")
    @Expose
    private String waqfPropertyName;
    @SerializedName("notification_no")
    @Expose
    private String notificationNo;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("nla_a")
    @Expose
    private Double nlaA;
    @SerializedName("nla_k")
    @Expose
    private Double nlaK;
    @SerializedName("nla_m")
    @Expose
    private Double nlaM;
    @SerializedName("cult_a")
    @Expose
    private Double cultA;
    @SerializedName("cult_k")
    @Expose
    private Double cultK;
    @SerializedName("cult_m")
    @Expose
    private Double cultM;
    @SerializedName("ncult_a")
    @Expose
    private Double ncultA;
    @SerializedName("ncult_k")
    @Expose
    private Double ncultK;
    @SerializedName("ncult_m")
    @Expose
    private Double ncultM;
    @SerializedName("shops")
    @Expose
    private Integer shops;
    @SerializedName("houses")
    @Expose
    private Integer houses;
    @SerializedName("others")
    @Expose
    private Integer others;
    @SerializedName("is_surveyed")
    @Expose
    private Integer isSurveyed;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    public final static Creator<AuqafProperty> CREATOR = new Creator<AuqafProperty>() {


        @SuppressWarnings({
                "unchecked"
        })
        public AuqafProperty createFromParcel(android.os.Parcel in) {
            return new AuqafProperty(in);
        }

        public AuqafProperty[] newArray(int size) {
            return (new AuqafProperty[size]);
        }

    }
            ;
    private final static long serialVersionUID = 7447875904194156376L;

    protected AuqafProperty(android.os.Parcel in) {
        this.zone = ((String) in.readValue((String.class.getClassLoader())));
        this.circle = ((String) in.readValue((String.class.getClassLoader())));
        this.waqfPropertyName = ((String) in.readValue((String.class.getClassLoader())));
        this.notificationNo = ((String) in.readValue((String.class.getClassLoader())));
        this.location = ((String) in.readValue((String.class.getClassLoader())));
        this.category = ((String) in.readValue((String.class.getClassLoader())));
        this.nlaA = ((Double) in.readValue((Double.class.getClassLoader())));
        this.nlaK = ((Double) in.readValue((Double.class.getClassLoader())));
        this.nlaM = ((Double) in.readValue((Double.class.getClassLoader())));
        this.cultA = ((Double) in.readValue((Double.class.getClassLoader())));
        this.cultK = ((Double) in.readValue((Double.class.getClassLoader())));
        this.cultM = ((Double) in.readValue((Double.class.getClassLoader())));
        this.ncultA = ((Double) in.readValue((Double.class.getClassLoader())));
        this.ncultK = ((Double) in.readValue((Double.class.getClassLoader())));
        this.ncultM = ((Double) in.readValue((Double.class.getClassLoader())));
        this.shops = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.houses = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.others = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.isSurveyed = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.remarks = ((String) in.readValue((String.class.getClassLoader())));
    }

    public AuqafProperty() {
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getWaqfPropertyName() {
        return waqfPropertyName;
    }

    public void setWaqfPropertyName(String waqfPropertyName) {
        this.waqfPropertyName = waqfPropertyName;
    }

    public String getNotificationNo() {
        return notificationNo;
    }

    public void setNotificationNo(String notificationNo) {
        this.notificationNo = notificationNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getNlaA() {
        return nlaA;
    }

    public void setNlaA(Double nlaA) {
        this.nlaA = nlaA;
    }

    public Double getNlaK() {
        return nlaK;
    }

    public void setNlaK(Double nlaK) {
        this.nlaK = nlaK;
    }

    public Double getNlaM() {
        return nlaM;
    }

    public void setNlaM(Double nlaM) {
        this.nlaM = nlaM;
    }

    public Double getCultA() {
        return cultA;
    }

    public void setCultA(Double cultA) {
        this.cultA = cultA;
    }

    public Double getCultK() {
        return cultK;
    }

    public void setCultK(Double cultK) {
        this.cultK = cultK;
    }

    public Double getCultM() {
        return cultM;
    }

    public void setCultM(Double cultM) {
        this.cultM = cultM;
    }

    public Double getNcultA() {
        return ncultA;
    }

    public void setNcultA(Double ncultA) {
        this.ncultA = ncultA;
    }

    public Double getNcultK() {
        return ncultK;
    }

    public void setNcultK(Double ncultK) {
        this.ncultK = ncultK;
    }

    public Double getNcultM() {
        return ncultM;
    }

    public void setNcultM(Double ncultM) {
        this.ncultM = ncultM;
    }

    public Integer getShops() {
        return shops;
    }

    public void setShops(Integer shops) {
        this.shops = shops;
    }

    public Integer getHouses() {
        return houses;
    }

    public void setHouses(Integer houses) {
        this.houses = houses;
    }

    public Integer getOthers() {
        return others;
    }

    public void setOthers(Integer others) {
        this.others = others;
    }

    public Integer getIsSurveyed() {
        return isSurveyed;
    }

    public void setIsSurveyed(Integer isSurveyed) {
        this.isSurveyed = isSurveyed;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(zone);
        dest.writeValue(circle);
        dest.writeValue(waqfPropertyName);
        dest.writeValue(notificationNo);
        dest.writeValue(location);
        dest.writeValue(category);
        dest.writeValue(nlaA);
        dest.writeValue(nlaK);
        dest.writeValue(nlaM);
        dest.writeValue(cultA);
        dest.writeValue(cultK);
        dest.writeValue(cultM);
        dest.writeValue(ncultA);
        dest.writeValue(ncultK);
        dest.writeValue(ncultM);
        dest.writeValue(shops);
        dest.writeValue(houses);
        dest.writeValue(others);
        dest.writeValue(isSurveyed);
        dest.writeValue(remarks);
    }

    public int describeContents() {
        return 0;
    }

}
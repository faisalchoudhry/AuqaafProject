
package ubaidgul.appdeveloper.auqaf.helper;

import java.io.Serializable;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AttachedProperty implements Serializable, Parcelable
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
    @SerializedName("area_name")
    @Expose
    private String areaName;
    @SerializedName("notification_no")
    @Expose
    private String notificationNo;
    @SerializedName("register_sr_no")
    @Expose
    private Integer registerSrNo;
    @SerializedName("landuse_type")
    @Expose
    private String landuseType;
    @SerializedName("land_sequence")
    @Expose
    private String landSequence;
    @SerializedName("attached_property_adress")
    @Expose
    private String attachedPropertyAdress;
    @SerializedName("area_m_f")
    @Expose
    private String areaMF;
    @SerializedName("leasee_name")
    @Expose
    private String leaseeName;
    @SerializedName("leasee_f_name")
    @Expose
    private String leaseeFName;
    @SerializedName("leasee_address")
    @Expose
    private String leaseeAddress;
    @SerializedName("rental_value_per_month")
    @Expose
    private Integer rentalValuePerMonth;
    @SerializedName("lease_start_date")
    @Expose
    private String leaseStartDate;
    @SerializedName("lease_end_date")
    @Expose
    private String leaseEndDate;
    @SerializedName("data_team_remarks")
    @Expose
    private String dataTeamRemarks;
    @SerializedName("is_surveyed")
    @Expose
    private Integer isSurveyed;
    public final static Creator<AttachedProperty> CREATOR = new Creator<AttachedProperty>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AttachedProperty createFromParcel(android.os.Parcel in) {
            return new AttachedProperty(in);
        }

        public AttachedProperty[] newArray(int size) {
            return (new AttachedProperty[size]);
        }

    }
    ;
    private final static long serialVersionUID = 876982271433049746L;

    protected AttachedProperty(android.os.Parcel in) {
        this.zone = ((String) in.readValue((String.class.getClassLoader())));
        this.circle = ((String) in.readValue((String.class.getClassLoader())));
        this.waqfPropertyName = ((String) in.readValue((String.class.getClassLoader())));
        this.areaName = ((String) in.readValue((String.class.getClassLoader())));
        this.notificationNo = ((String) in.readValue((String.class.getClassLoader())));
        this.registerSrNo = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.landuseType = ((String) in.readValue((String.class.getClassLoader())));
        this.landSequence = ((String) in.readValue((String.class.getClassLoader())));
        this.attachedPropertyAdress = ((String) in.readValue((String.class.getClassLoader())));
        this.areaMF = ((String) in.readValue((String.class.getClassLoader())));
        this.leaseeName = ((String) in.readValue((String.class.getClassLoader())));
        this.leaseeFName = ((String) in.readValue((String.class.getClassLoader())));
        this.leaseeAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.rentalValuePerMonth = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.leaseStartDate = ((String) in.readValue((String.class.getClassLoader())));
        this.leaseEndDate = ((String) in.readValue((String.class.getClassLoader())));
        this.dataTeamRemarks = ((String) in.readValue((String.class.getClassLoader())));
        this.isSurveyed = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public AttachedProperty() {
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

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getNotificationNo() {
        return notificationNo;
    }

    public void setNotificationNo(String notificationNo) {
        this.notificationNo = notificationNo;
    }

    public Integer getRegisterSrNo() {
        return registerSrNo;
    }

    public void setRegisterSrNo(Integer registerSrNo) {
        this.registerSrNo = registerSrNo;
    }

    public String getLanduseType() {
        return landuseType;
    }

    public void setLanduseType(String landuseType) {
        this.landuseType = landuseType;
    }

    public String getLandSequence() {
        return landSequence;
    }

    public void setLandSequence(String landSequence) {
        this.landSequence = landSequence;
    }

    public String getAttachedPropertyAdress() {
        return attachedPropertyAdress;
    }

    public void setAttachedPropertyAdress(String attachedPropertyAdress) {
        this.attachedPropertyAdress = attachedPropertyAdress;
    }

    public String getAreaMF() {
        return areaMF;
    }

    public void setAreaMF(String areaMF) {
        this.areaMF = areaMF;
    }

    public String getLeaseeName() {
        return leaseeName;
    }

    public void setLeaseeName(String leaseeName) {
        this.leaseeName = leaseeName;
    }

    public String getLeaseeFName() {
        return leaseeFName;
    }

    public void setLeaseeFName(String leaseeFName) {
        this.leaseeFName = leaseeFName;
    }

    public String getLeaseeAddress() {
        return leaseeAddress;
    }

    public void setLeaseeAddress(String leaseeAddress) {
        this.leaseeAddress = leaseeAddress;
    }

    public Integer getRentalValuePerMonth() {
        return rentalValuePerMonth;
    }

    public void setRentalValuePerMonth(Integer rentalValuePerMonth) {
        this.rentalValuePerMonth = rentalValuePerMonth;
    }

    public String getLeaseStartDate() {
        return leaseStartDate;
    }

    public void setLeaseStartDate(String leaseStartDate) {
        this.leaseStartDate = leaseStartDate;
    }

    public String getLeaseEndDate() {
        return leaseEndDate;
    }

    public void setLeaseEndDate(String leaseEndDate) {
        this.leaseEndDate = leaseEndDate;
    }

    public String getDataTeamRemarks() {
        return dataTeamRemarks;
    }

    public void setDataTeamRemarks(String dataTeamRemarks) {
        this.dataTeamRemarks = dataTeamRemarks;
    }

    public Integer getIsSurveyed() {
        return isSurveyed;
    }

    public void setIsSurveyed(Integer isSurveyed) {
        this.isSurveyed = isSurveyed;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(zone);
        dest.writeValue(circle);
        dest.writeValue(waqfPropertyName);
        dest.writeValue(areaName);
        dest.writeValue(notificationNo);
        dest.writeValue(registerSrNo);
        dest.writeValue(landuseType);
        dest.writeValue(landSequence);
        dest.writeValue(attachedPropertyAdress);
        dest.writeValue(areaMF);
        dest.writeValue(leaseeName);
        dest.writeValue(leaseeFName);
        dest.writeValue(leaseeAddress);
        dest.writeValue(rentalValuePerMonth);
        dest.writeValue(leaseStartDate);
        dest.writeValue(leaseEndDate);
        dest.writeValue(dataTeamRemarks);
        dest.writeValue(isSurveyed);
    }

    public int describeContents() {
        return  0;
    }

}

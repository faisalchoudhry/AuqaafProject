package ubaidgul.appdeveloper.auqaf.upload_later;

public class Item {

    public int Id;
    public String iconFile;
    public int localIDs;
    public String circleIDS;
    public String srNumIDS;
    public String puNumIDS;
    public String localityIDS;
    public String wardIDS;
    public String blockIDS;
    public String DateTimeIDS;


    public Item(int id, String iconFile, int localIDs, String circleIDS, String srNumIDS, String puNumIDS, String localityIDS, String wardIDS, String blockIDS, String DateTimeIDS) {
        Id = id;
        this.iconFile = iconFile;
        this.localIDs = localIDs;
        this.circleIDS = circleIDS;
        this.srNumIDS = srNumIDS;
        this.puNumIDS = puNumIDS;
        this.localityIDS = localityIDS;
        this.wardIDS = wardIDS;
        this.blockIDS = blockIDS;
        this.DateTimeIDS = DateTimeIDS;
    }

}

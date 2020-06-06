package ro.atm.dmc.objectselector.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "Photos")
public class Photo {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "ImagePath")
    @NonNull
    private String imagePath;

    @ColumnInfo(name = "SendDate")
    @NonNull
    private String sendDate;

    @ColumnInfo(name = "ClassCount")
    @NonNull
    private int classCount;

    @ColumnInfo(name = "Longitude")
    @NonNull
    private double longitude;

    @ColumnInfo(name = "Latitude")
    @NonNull
    private double latitude;

    public Photo(@NonNull String imagePath, @NonNull String sendDate, int classCount, double longitude, double latitude) {
        this.imagePath = imagePath;
        this.sendDate = sendDate;
        this.classCount = classCount;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void setLocation(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(@NonNull String imagePath) {
        this.imagePath = imagePath;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(@NonNull String sendDate) {
        this.sendDate = sendDate;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getClassCount() {
        return classCount;
    }

    public void setClassCount(int classCount) {
        this.classCount = classCount;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(classCount + " classes\n\n");
        stringBuilder.append("Send Date: " + sendDate + "\n\n");
        if (longitude != -1)
            stringBuilder.append("Longitude: " + longitude + "\n\n");
        if (latitude != -1)
            stringBuilder.append("Latitude: " + latitude + "\n\n");

        return stringBuilder.toString();
    }
}

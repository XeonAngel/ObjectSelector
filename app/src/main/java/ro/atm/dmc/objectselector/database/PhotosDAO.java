package ro.atm.dmc.objectselector.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PhotosDAO {

    @Insert
    void insert(Photo photo);

    @Update
    void update(Photo photo);

    @Delete
    void delete(Photo photo);

    @Query("SELECT * FROM Photos")
    List<Photo> selectAll();

    @Query("SELECT COUNT(id) FROM Photos WHERE ClassCount = :classCount")
    int countPhotos(int classCount);

    @Query("SELECT MAX(ClassCount) FROM Photos")
    int maxClassCount();

    @Query("SELECT COUNT(id) FROM Photos")
    int numberOfPhotos();
}

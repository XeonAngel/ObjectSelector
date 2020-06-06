package ro.atm.dmc.objectselector.database;

import android.content.Context;

import java.util.List;

public class PhotoDataSource {
    private PhotosDAO photosDAO;
    private List<Photo> photoList;

    public PhotoDataSource(Context context) {
        PhotoDatabase photoDatabase = PhotoDatabase.getDatabase(context);
        photosDAO = photoDatabase.photosDAO();
        photoList = photosDAO.selectAll();
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void insert(final Photo photo) {
        PhotoDatabase.databaseSERVICE.execute(new Runnable() {
            @Override
            public void run() {
                photosDAO.insert(photo);
                photoList = photosDAO.selectAll();
            }
        });
    }
}

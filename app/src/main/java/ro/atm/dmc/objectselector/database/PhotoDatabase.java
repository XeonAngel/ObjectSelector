package ro.atm.dmc.objectselector.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Photo.class}, version = 1, exportSchema = false)
public abstract class PhotoDatabase extends RoomDatabase {

    public abstract PhotosDAO photosDAO();

    private static volatile PhotoDatabase DATABASE;
    private static final String DATABASE_NAME = "photo_Database";

    private static final int THREADS_NUMBER = 4;
    static final ExecutorService databaseSERVICE = Executors.newFixedThreadPool(THREADS_NUMBER);

    static synchronized PhotoDatabase getDatabase(final Context context) {
        if (DATABASE == null) {
            DATABASE = Room.databaseBuilder(
                    context.getApplicationContext(), PhotoDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .setQueryExecutor(databaseSERVICE)
                    .build();
        }
        return DATABASE;
    }
}

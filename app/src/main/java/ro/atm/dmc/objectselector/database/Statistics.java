package ro.atm.dmc.objectselector.database;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.Random;

public class Statistics extends View {

    Context context;

    public Statistics(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Random rnd = new Random();

        PhotoDatabase photoDatabase = PhotoDatabase.getDatabase(context);
        PhotosDAO photosDAO = photoDatabase.photosDAO();

        int maxClassCount = photosDAO.maxClassCount();
        int photosNumber = photosDAO.numberOfPhotos();

        Paint paint = new Paint();
        float unghiStart = 0;
        float unglePerPhoto = 360 / photosNumber;
        for (int i = 0; i <= maxClassCount; i++) {
            paint.setColor(Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
            int photosWithICLasses = photosDAO.countPhotos(i);
            float unghi = photosWithICLasses * unglePerPhoto;
            canvas.drawArc(250, 250, 800, 800, unghiStart, unghi, true, paint);
            unghiStart += unghi;
        }
    }
}

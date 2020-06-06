package ro.atm.dmc.objectselector;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ro.atm.dmc.objectselector.database.ObjectRectangle;
import ro.atm.dmc.objectselector.database.Photo;
import ro.atm.dmc.objectselector.database.PhotoDataSource;

public class UploadPhotoActivity extends AppCompatActivity {

    private PhotoDataSource photoDataSource;

    public static final int CODE_ADD_LOCATION = 1;
    private String imagePath = null;
    private List<ObjectRectangle> objectRectangleList;
    private double locationLongitude = -1;
    private double locationLatitude = -1;

    private ImageView imageResult, imageDrawingPane;
    private Bitmap bitmapMaster;
    private Canvas canvasMaster;
    private Bitmap bitmapDrawingPane;
    private Canvas canvasDrawingPane;
    private projectPt startPt = new projectPt(-1, -1);
    private projectPt endPt = new projectPt(-1, -1);
    boolean isClassAdded = true;

    class projectPt {
        int x;
        int y;

        projectPt(int tx, int ty) {
            x = tx;
            y = ty;
        }
    }

    private projectPt projectXY(ImageView iv, Bitmap bm, int x, int y) {
        if (x < 0 || y < 0 || x > iv.getWidth() || y > iv.getHeight()) {
            //outside ImageView
            return null;
        } else {
            int projectedX = (int) ((double) x * ((double) bm.getWidth() / (double) iv.getWidth()));
            int projectedY = (int) ((double) y * ((double) bm.getHeight() / (double) iv.getHeight()));

            return new projectPt(projectedX, projectedY);
        }
    }

    private void drawOnRectProjectedBitMap(ImageView iv, Bitmap bm, int x, int y) {
        if (x < 0 || y < 0 || x > iv.getWidth() || y > iv.getHeight()) {
            //outside ImageView
            return;
        } else {
            int projectedX = (int) ((double) x * ((double) bm.getWidth() / (double) iv.getWidth()));
            int projectedY = (int) ((double) y * ((double) bm.getHeight() / (double) iv.getHeight()));

            //clear canvasDrawingPane
            canvasDrawingPane.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(3);
            canvasDrawingPane.drawRect(startPt.x, startPt.y, projectedX, projectedY, paint);
            imageDrawingPane.invalidate();
        }
    }

    private void finalizeDrawing() {
        canvasMaster.drawBitmap(bitmapDrawingPane, 0, 0, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                imagePath = null;
            } else {
                imagePath = extras.getString("SelectedImagePath");
            }
        } else {
            imagePath = (String) savedInstanceState.getSerializable("SelectedImagePath");
        }
        imageResult = findViewById(R.id.uploadImageView);
        imageDrawingPane = findViewById(R.id.drawingpane);

        Bitmap tempBitmap;

        //tempBitmap is Immutable bitmap,
        //cannot be passed to Canvas constructor
        tempBitmap = BitmapFactory.decodeFile(imagePath);

        Bitmap.Config config;
        if (tempBitmap.getConfig() != null) {
            config = tempBitmap.getConfig();
        } else {
            config = Bitmap.Config.ARGB_8888;
        }

        //bitmapMaster is Mutable bitmap
        bitmapMaster = Bitmap.createBitmap(
                tempBitmap.getWidth(),
                tempBitmap.getHeight(),
                config);

        canvasMaster = new Canvas(bitmapMaster);
        canvasMaster.drawBitmap(tempBitmap, 0, 0, null);

        imageResult.setImageBitmap(bitmapMaster);

        //Create bitmap of same size for drawing
        bitmapDrawingPane = Bitmap.createBitmap(
                tempBitmap.getWidth(),
                tempBitmap.getHeight(),
                config);
        canvasDrawingPane = new Canvas(bitmapDrawingPane);
        imageDrawingPane.setImageBitmap(bitmapDrawingPane);

        imageResult.setOnTouchListener((view, motionEvent) -> {
            if (isClassAdded == false)
                return false;
            int action = motionEvent.getAction();
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    startPt = projectXY((ImageView) view, bitmapMaster, x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    drawOnRectProjectedBitMap((ImageView) view, bitmapMaster, x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    drawOnRectProjectedBitMap((ImageView) view, bitmapMaster, x, y);
                    endPt = new projectPt(x, y);
                    isClassAdded = false;
                    finalizeDrawing();
                    break;
            }
            return true;
        });
        objectRectangleList = new ArrayList<>();
        this.photoDataSource = new PhotoDataSource(getApplicationContext());
    }

    public void clickOnSendPhoto(View view) {
        String serverIp = PreferencesInterface.readPrefString(getApplicationContext(),
                "" + R.string.serverIpPreferenceName);
        int serverPort = PreferencesInterface.readPrefInt(getApplicationContext(),
                "" + R.string.serverPortPreferenceName);

        if (serverIp.isEmpty() || serverPort == -1) {
            Toast.makeText(getApplicationContext(), "Insert valid server ip and port!"
                    , Toast.LENGTH_SHORT).show();
            return;
        }

        String imagePostUrl = "http://" + serverIp + ":" + serverPort + "/uploadImage";
        String csvPostUrl = "http://" + serverIp + ":" + serverPort + "/uploadCvs";

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        StringBuilder csvBody = new StringBuilder();
        for (ObjectRectangle rect : objectRectangleList) {
            csvBody.append(rect.toString());
        }

        String imageName = UUID.randomUUID().toString();

        RequestBody postBodyImage = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", imageName + ".jpg"
                        , RequestBody.create(MediaType.parse("image/*jpg"), byteArray))
                .build();
        RequestBody postBodyCsv = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("csv", imageName + ".csv"
                        , RequestBody.create(MediaType.parse("text/plain"), csvBody.toString()))
                .build();

        Toast.makeText(getApplicationContext(), "Please wait ...", Toast.LENGTH_SHORT).show();

        postRequest(imagePostUrl, postBodyImage);
        postRequest(csvPostUrl, postBodyCsv);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Photo photo = new Photo(imagePath
                , simpleDateFormat.format(Calendar.getInstance().getTime())
                , objectRectangleList.size()
                , locationLongitude
                , locationLatitude);
        addPhoto(photo);

        Intent answer = new Intent(getApplicationContext(), MainActivity.class);
        this.setResult(RESULT_OK, answer);
        this.finish();
    }

    public void addLocation(View view) {
        Intent intent = new Intent(getApplicationContext(), AddLocationActivity.class);
        startActivityForResult(intent, CODE_ADD_LOCATION);
    }

    public void addClass(View view) {
        TextView textView = findViewById(R.id.classNameEditText);
        if (textView.getText().toString().equals("Class Name")) {
            Toast.makeText(getApplicationContext(),
                    "Insert class name",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (endPt.y == -1 || endPt.x == -1) {
            Toast.makeText(getApplicationContext(),
                    "Draw the rectangle around your object",
                    Toast.LENGTH_SHORT).show();
        }
        objectRectangleList.add(new ObjectRectangle(objectRectangleList.size() + 1,
                textView.getText().toString(),
                startPt.x, startPt.y,
                endPt.x, endPt.y));
        isClassAdded = true;
        textView.setText("Class Name");
        endPt.y = -1;
        endPt.x = -1;
    }

    public void addPhoto(Photo photo) {
        photoDataSource.insert(photo);
    }

    void postRequest(String postUrl, RequestBody postBody) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Cancel the post on failure.
                call.cancel();

                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Failed to Connect to Server", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Toast.makeText(getApplicationContext(),
                                    response.body().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_ADD_LOCATION && data != null) {
            this.locationLongitude = data.getDoubleExtra("LocationLongitude", -1);
            this.locationLatitude = data.getDoubleExtra("LocationLatitude", -1);

            Toast.makeText(getApplicationContext(),
                    "Long: " +
                            locationLongitude +
                            "\nLat: " +
                            locationLatitude,
                    Toast.LENGTH_SHORT).show();
        }
    }
}

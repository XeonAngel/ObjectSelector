package ro.atm.dmc.objectselector.adaptors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import ro.atm.dmc.objectselector.R;
import ro.atm.dmc.objectselector.database.Photo;
import ro.atm.dmc.objectselector.database.PhotoDataSource;

public class PhotoAdapter extends BaseAdapter {
    private Context context;
    private PhotoDataSource photoDataSource;

    public PhotoAdapter(Context context) {
        this.context = context;
        this.photoDataSource = new PhotoDataSource(context);
    }

    @Override
    public int getCount() {
        return photoDataSource.getPhotoList().size();
    }

    @Override
    public Object getItem(int position) {
        return photoDataSource.getPhotoList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return photoDataSource.getPhotoList().get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View viewPhotos = inflater.inflate(R.layout.photo_item, viewGroup, false);

        TextView textView = viewPhotos.findViewById(R.id.detailsTextView);
        Photo photo = (Photo) getItem(position);
        textView.setText(photo.toString());

        ImageView imageView = viewPhotos.findViewById(R.id.photoImageView);
        File imgFile = new File(photo.getImagePath());

        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }

        return viewPhotos;
    }
}

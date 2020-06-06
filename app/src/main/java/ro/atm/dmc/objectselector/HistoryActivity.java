package ro.atm.dmc.objectselector;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import ro.atm.dmc.objectselector.adaptors.PhotoAdapter;

public class HistoryActivity extends AppCompatActivity {

    PhotoAdapter photoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        photoAdapter = new PhotoAdapter(getApplicationContext());
        ListView listView = findViewById(R.id.photoListView);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setAdapter(photoAdapter);
    }
}

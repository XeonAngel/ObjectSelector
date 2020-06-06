package ro.atm.dmc.objectselector;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import ro.atm.dmc.objectselector.database.Statistics;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = new Statistics(this);
        view.setBackgroundColor(0xFF03DAC6);
        setContentView(view);
    }
}

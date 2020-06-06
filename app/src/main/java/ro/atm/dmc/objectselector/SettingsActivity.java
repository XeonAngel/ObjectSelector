package ro.atm.dmc.objectselector;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        String[] languageList = new String[]{
                "English", "Romanian"};

        Spinner s = findViewById(R.id.languageSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, languageList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        loadValues();
    }

    public void clickOnSave(View view) {
        Intent answer = new Intent();

        EditText editIp = findViewById(R.id.serverIpEditText);
        EditText editPort = findViewById(R.id.serverPortEditText);

        String serverIP = editIp.getText().toString();
        int serverPort;
        String svPort = editPort.getText().toString();
        if (svPort.isEmpty())
            serverPort = -1;
        else
            serverPort = Integer.parseInt(svPort);
        PreferencesInterface.writePrefString(getApplicationContext(),
                "" + R.string.serverIpPreferenceName, serverIP);
        PreferencesInterface.writePrefInt(getApplicationContext(),
                "" + R.string.serverPortPreferenceName, serverPort);

        this.setResult(RESULT_OK, answer);
        this.finish();
    }

    private void loadValues() {

        String serverIP = PreferencesInterface.readPrefString(getApplicationContext(),
                "" + R.string.serverIpPreferenceName);
        int serverPort = PreferencesInterface.readPrefInt(getApplicationContext(),
                "" + R.string.serverPortPreferenceName);

        EditText editIp = findViewById(R.id.serverIpEditText);
        EditText editPort = findViewById(R.id.serverPortEditText);

        editIp.setText(serverIP);
        if (serverPort == -1)
            editPort.setText("");
        else
            editPort.setText(String.valueOf(serverPort));
    }

    public void clickOnTestConnection(View view) {
        EditText editIp = findViewById(R.id.serverIpEditText);
        EditText editPort = findViewById(R.id.serverPortEditText);
        String serverIP = editIp.getText().toString();
        String serverPort = editPort.getText().toString();

        final String[] serverState = new String[]{""};
        TestServerTask testServerTask;
        testServerTask = new TestServerTask() {
            @Override
            protected void onPostExecute(String svState) {
                serverState[0] = svState;
                super.onPostExecute(svState);
            }
        };

        try {
            testServerTask.execute("http://" + serverIP + ":" + serverPort + "/serverState").get(2, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
            Toast.makeText(this, "Execution Fail", Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(this, "Interrupt", Toast.LENGTH_SHORT).show();
        } catch (TimeoutException e) {
            e.printStackTrace();
            Toast.makeText(this, "Timeout", Toast.LENGTH_SHORT).show();
        }

        if (serverState[0].equals("true"))
            Toast.makeText(this, "Server Up", Toast.LENGTH_SHORT).show();
        else
            //Toast.makeText(this, "Server Down", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Server Up", Toast.LENGTH_SHORT).show();

    }
}

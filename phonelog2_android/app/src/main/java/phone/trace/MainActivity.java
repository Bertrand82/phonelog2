package phone.trace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();

    ApplicationBg applicationBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.applicationBg = (ApplicationBg) this.getApplication();
        setContentView(R.layout.activity_main);
    }
}
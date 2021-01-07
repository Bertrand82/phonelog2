package phone.crm2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ActivitySettingsBg extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.setting_container, new FragmentBgSetting())
                .commit();
    }


    }

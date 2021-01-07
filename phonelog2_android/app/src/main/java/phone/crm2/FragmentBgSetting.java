package phone.crm2;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class FragmentBgSetting extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_bg, rootKey);
    }
}

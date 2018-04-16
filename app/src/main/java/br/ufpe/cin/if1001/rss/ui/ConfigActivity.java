package br.ufpe.cin.if1001.rss.ui;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import br.ufpe.cin.if1001.rss.R;

public class ConfigActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
    }

    public static class ConfigFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.config);
        }
    }
}

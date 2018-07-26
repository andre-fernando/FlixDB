package comandre_fernando.httpsgithub.flixdb.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import java.util.Objects;

import comandre_fernando.httpsgithub.flixdb.R;
import comandre_fernando.httpsgithub.flixdb.components.viewmodels.MainViewModel;
import comandre_fernando.httpsgithub.flixdb.roomdb.FavDB;


public class Settings_Fragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);
        init();
    }

    private void init(){
        final MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        //Set's summary of Default Lang and adds OnChangeListener
        final ListPreference lp_default_lang = (ListPreference) getPreferenceManager().findPreference(getString(R.string.pref_default_lang_key));
        lp_default_lang.setSummary(((ListPreference) getPreferenceManager().findPreference(getString(R.string.pref_default_lang_key))).getEntry());
        lp_default_lang.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (preference instanceof ListPreference){
                    ListPreference lp = (ListPreference) preference;
                    int index =lp.findIndexOfValue((String) newValue);
                    CharSequence[] entries = lp.getEntries();
                    mainViewModel.setDefaultLanguage((String) newValue);
                    lp_default_lang.setSummary(entries[index]);
                    return true;
                }
                return false;
            }
        });

        //Show adult On Change Listener
        CheckBoxPreference cb_allow_adult = (CheckBoxPreference) getPreferenceManager().findPreference(getString(R.string.pref_allow_adult_key));
        cb_allow_adult.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (preference instanceof CheckBoxPreference){
                    mainViewModel.setShowAdultContent((boolean)newValue);
                    return true;
                }
                return false;
            }
        });

        // Preference to clear favourites
        Preference p_clearfav = getPreferenceManager().findPreference(getString(R.string.pref_clearfavourites_key));
        p_clearfav.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                initAlertDialog();
                return true;
            }
        });
    }

    /**
     * This method creates an alert dialog to confirm to clear favourites
     */
    private void initAlertDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder
                (Objects.requireNonNull(getContext()));
        alert.setTitle(R.string.alert_clearfav_title)
                .setMessage(R.string.alert_clearfav_message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FavDB.getInstance(getContext()).clearFavourites();
                        Toast.makeText(getContext(),
                                "Favourites cleared!!",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}

package com.aero.control.prefs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.aero.control.R;

/**
 * Created by Alexander Christ on 21.09.13.
 */
public class PrefsActivity extends PreferenceActivity {

    static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.layout.preference);

        // Get name;
        this.setTitle(R.string.aero_settings);

        context = this;

        getActionBar().setDisplayHomeAsUpEnabled(true);

        final PreferenceScreen root = this.getPreferenceScreen();
        String[] data = {
                "red", "light"
        };

        EditTextPreference updateLocation = (EditTextPreference)root.findPreference("update_location");
        CheckBoxPreference checkbox_preference = (CheckBoxPreference)root.findPreference("checkbox_preference");
        ListPreference appTheme = (ListPreference)root.findPreference("app_theme_list");
        Preference about = (Preference)root.findPreference("about");

        updateLocation.setEnabled(false);
        updateLocation.setIcon(R.drawable.ic_action_settings);

        checkbox_preference.setIcon(R.drawable.ic_action_warning);

        appTheme.setEntries(data);
        appTheme.setEntryValues(data);
        appTheme.setEnabled(false);
        appTheme.setIcon(R.drawable.ic_action_event);

        about.setIcon(R.drawable.ic_action_about);

        appTheme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                String a = (String) o;

                // Somehow set the style here....;
                if (a.equals("red")) {
                    //this.setTheme(R.style.RedHolo);
                    root.setLayoutResource(R.style.RedHolo);
                }
                else if (a.equals("light")) {
                    //getActivity().setTheme(android.R.style.Theme_Holo_Light);
                    root.setLayoutResource(android.R.style.Theme_Holo_Light);
                }

                return true;
            };
        });

        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater)getLayoutInflater();
                View layout = inflater.inflate(R.layout.about_screen, null);
                TextView aboutText = (TextView) layout.findViewById(R.id.aboutScreen);

                builder.setTitle(R.string.about);
                builder.setIcon(R.drawable.email_dark);

                aboutText.setText(getText(R.string.about_dialog));

                builder.setView(layout)
                        .setPositiveButton(R.string.github, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                Uri uri = Uri.parse("https://github.com/Blechd0se/android_packages_apps_AeroControl");
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.donation_blechdose, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Uri uri = Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=46VQEKBETN36U");
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        })
                        .setNeutralButton(R.string.donation_quarx, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Uri uri = Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=quarx%40yandex%2eru&lc=DE&no_note=0&currency_code=EUR&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHostedGuest");
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        });

                builder.show();


                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
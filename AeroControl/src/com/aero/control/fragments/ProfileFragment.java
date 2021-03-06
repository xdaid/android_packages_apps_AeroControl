package com.aero.control.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.renderscript.Sampler;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.aero.control.R;

import java.io.File;
import java.util.Map;
import java.util.Set;

/**
 * Created by Alexander Christ on 10.10.13.
 */
public class ProfileFragment extends PreferenceFragment {

    /*
     * TODO: - Make survival over fragment switch
     *
     */

    private ViewGroup mContainerView;
    private SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        final View v = inflater.inflate(R.layout.profile_fragment, null);

        mContainerView = (ViewGroup)v.findViewById(R.id.container);


        // Load default profiles;
        addDefaultProfiles(new EditText(getActivity()));

        return mContainerView;
    }

    public void addDefaultProfiles(EditText editText) {

        File prefFile = new File ("/data/data/" + getActivity().getPackageName() + "/shared_prefs/" + "performance.xml");
        if(prefFile.exists()) {
            Log.e("Aero", "Performance Profile exists already!");
        } else {
            Log.e("Aero", "Performance Profile doesn't exist!");
            editText.setText("performance");
            addProfile(editText.getText().toString());
        }

    }

    // Create our options menu;
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profiles_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_item:
                // Hide the "empty" view since there is now at least one item in the list.
                mContainerView.findViewById(android.R.id.empty).setVisibility(View.GONE);

                showDialog(new EditText(getActivity()));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialog(final EditText editText) {

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.add_a_name)
                .setMessage(R.string.define_a_name)
                .setView(editText)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Add content;
                        addProfile(editText.getText().toString());

                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();

        dialog.show();
    }

    // Adds the object to our "list", s = Name
    private void addProfile(String s) {

        // Create custom SharedPreference for profile
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences newProfile = getActivity().getSharedPreferences(s, Context.MODE_PRIVATE);

        // This will save the current profile as a preference;
        saveProfile(newProfile);

        // Instantiate a new "row" view.
        final ViewGroup childView = (ViewGroup) LayoutInflater.from(getActivity()).inflate(
                R.layout.profiles_list, mContainerView, false);

        // Create TextView, with Content and Listeners;
        TextView txtView = (TextView)childView.findViewById(R.id.profile_text);
        txtView.setText(s);
        createListener(txtView);

        // Remove the complete ViewGroup;
        childView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mContainerView.removeView(childView);

                // If there are no rows remaining, show the empty view.
                if (mContainerView.getChildCount() == 1) {
                    mContainerView.findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
                }
            }
        });

        mContainerView.addView(childView, 0);

    }

    private void saveProfile(SharedPreferences newProfile) {


        SharedPreferences.Editor editor = newProfile.edit();

        // Get all our preferences;
        Map<String,?> allKeys = prefs.getAll();

        for(Map.Entry<String, ?> entry : allKeys.entrySet()) {

            // We found a boolean, wow!
            if (entry.getValue().toString().equals("true") || entry.getValue().toString().equals("false")) {

                Boolean tmp = Boolean.getBoolean(entry.getValue().toString());

                editor.putBoolean(entry.getKey(), tmp);

            } else {
                editor.putString(entry.getKey(), entry.getValue().toString());
            }
        }

        editor.commit();

    }

    /*
     * Create a onClick Listener for each profile;
     */

    public void createListener(final TextView txtView) {

        // Change something else?
        txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //
            }

        });

        // Change the name of the profile;
        txtView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final EditText editText = new EditText(getActivity());
                editText.setText(txtView.getText());

                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Set another name")
                        .setMessage("Change the Name of the Profile")
                        .setView(editText)
                        .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                txtView.setText(editText.getText().toString());
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .create();
                dialog.show();

                return true;
            }

        });

    }

}

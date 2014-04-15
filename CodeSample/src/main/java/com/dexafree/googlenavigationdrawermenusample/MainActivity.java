/*
 * Sample project only for demonstration purposes
 *
 * This project uses the GoogleNavigationDrawerMenu Android library,
 * created by Jorge Martín (Arasthel), which is released under a
 * GPLv3 license:
 * https://github.com/Arasthel/GoogleNavigationDrawerMenu
 *
 * In this project you can see the library implementation made via Java code
 *
 * Sample project made by Dexafree
 */

package com.dexafree.googlenavigationdrawermenusample;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.*;
import org.arasthel.googlenavdrawermenu.views.GoogleNavigationDrawer;

public class MainActivity extends ActionBarActivity {

    private Context mContext;
    private ActionBarDrawerToggle drawerToggle;
    private GoogleNavigationDrawer mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;


        //First of all we need to prepare the LayoutParams
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        //Instance the GoogleNavigationDrawer and set the LayoutParams
        mDrawer = new GoogleNavigationDrawer(mContext);
        mDrawer.setLayoutParams(params);



        // Here we are providing data to the adapter of the ListView
        String[] mainSections = getResources().getStringArray(R.array.navigation_main_sections);
        String[] secondarySections = getResources().getStringArray(R.array.navigation_secondary_sections);
        int[] mainSectionDrawables = getDrawables();


        mDrawer.setListViewSections(mainSections, // Main sections
                secondarySections, // Secondary sections
                mainSectionDrawables, // Main sections icon ids
                null); // Secondary sections icon ids


        // Now we add the content to the drawer since the menu is already there
        LayoutInflater inflater = getLayoutInflater();
        View contentView = inflater.inflate(R.layout.main_content, null);
        mDrawer.addView(contentView, 0);


        setContentView(mDrawer);


        //Prepare the drawerToggle in order to be able to open/close the drawer
        drawerToggle = new ActionBarDrawerToggle(this,
                mDrawer,
                R.drawable.ic_drawer,
                R.string.app_name,
                R.string.app_name);


        //Attach the DrawerListener
        mDrawer.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
         * Declare the behaviour of clicking at the
         * application icon, opening and closing the drawer
         */
        if(item.getItemId() == android.R.id.home) {
            if(mDrawer != null) {
                if(mDrawer.isDrawerMenuOpen()) {
                    mDrawer.closeDrawerMenu();
                } else {
                    mDrawer.openDrawerMenu();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private int[] getDrawables(){
        TypedArray imgs = getResources().obtainTypedArray(R.array.drawable_ids);

        int[] mainSectionDrawables = new int[imgs.length()];

        for(int i=0;i<imgs.length();i++){
            mainSectionDrawables[i] = imgs.getResourceId(i, 0);
        }

        return mainSectionDrawables;
    }
}

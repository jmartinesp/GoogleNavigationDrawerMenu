/*
 * Sample project only for demonstration purposes
 *
 * This project uses the GoogleNavigationDrawerMenu Android library,
 * created by Jorge Martín (Arasthel), which is released under a
 * GPLv3 license:
 * https://github.com/Arasthel/GoogleNavigationDrawerMenu
 *
 * In this project you can see the library implementation made via XML code
 *
 * Sample project made by Dexafree
 */

package com.dexafree.googlenavigationdrawermenusample;

import android.content.Context;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.*;
import org.arasthel.googlenavdrawermenu.views.GoogleNavigationDrawer;

public class MainActivity extends ActionBarActivity {

    private ActionBarDrawerToggle drawerToggle;
    private GoogleNavigationDrawer mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * We get the GoogleNavigationDrawer object
         * in order to allow further method usage
         */
        mDrawer = (GoogleNavigationDrawer) findViewById(R.id.navigation_drawer_container);

        /*
         * We get the drawerToggle object order to
         * allow showing the NavigationDrawer icon
         */
        drawerToggle = new ActionBarDrawerToggle(this,
                mDrawer,
                R.drawable.ic_drawer,
                R.string.app_name,
                R.string.app_name);


        mDrawer.setDrawerListener(drawerToggle); //Attach the DrawerListener

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
}

package com.field.datamatics.views;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.field.datamatics.R;
import com.field.datamatics.utils.AppControllerUtil;
import com.field.datamatics.views.fragments.MonthlyVisit;
import com.field.datamatics.views.fragments.MrActions;
import com.field.datamatics.views.fragments.RoutePlan;
import com.field.datamatics.views.fragments.TodayVisit;
import com.field.datamatics.views.fragments.VisitHomePage;


/**
 * Created by anoop on 20/9/15.
 */
public class MrLandingPage extends BaseActivity{
    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Fragment fragment=null;
    private FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mr_landing_page);
        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpDrawer();
        loadFirstFragment();

    }

    private void setUpDrawer(){
        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.action_today_visit:
                        fragment = new TodayVisit();
                        setTitle("Today Visit");
                        break;
                    case R.id.action_route_plan:
                        fragment = new RoutePlan();
                        setTitle("Route Plan");
                        break;
                    case R.id.action_monthly_visit:
                        fragment = new MonthlyVisit();
                        setTitle("Monthly Visit");
                        break;
                    case R.id.action_pending_list:
                        //fragment = new TodayVisit();
                        //setTitle("Pending Visit");
                        break;
                    case R.id.action_core_card:
                        //fragment = new TodayVisit();
                        //setTitle("Pending Visit");
                        break;
                    case R.id.action_settings:
                        fragment=null;
                        break;
                    case R.id.action_log_out:
                        finish();
                        fragment=null;
                        break;


                    default:
                        break;
                }
                if (fragment != null) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fragmentTransaction.replace(R.id.frame, fragment);
                            fragmentTransaction.commit();
                        }
                    }, 500);

                }
                return true;

            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }
    private void loadFirstFragment() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragment = new TodayVisit();
        if (fragment != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.commit();
                    setTitle("Today Visit");
                }
            }, 500);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mr_home, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        onBackEvent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onBackEvent(){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(AppControllerUtil.getInstance().getCurrent_fragment().equals("TodayVisit")){
            finish();
        }
        else if(AppControllerUtil.getInstance().getCurrent_fragment().equals("VisitHomePage")){
            fragment=new TodayVisit();
            setTitle("Today Visit");

        }
        else if(AppControllerUtil.getInstance().getCurrent_fragment().equals("MrActions")){
            fragment=new VisitHomePage();

        }
        else if(AppControllerUtil.getInstance().getCurrent_fragment().equals("CustomProductList")){
            fragment=new MrActions();

        }
        else if(AppControllerUtil.getInstance().getCurrent_fragment().equals("Image_video_tab")){
            fragment=new MrActions();

        }
        else if(AppControllerUtil.getInstance().getCurrent_fragment().equals("SampleIntroduction")){
            fragment=new MrActions();

        }
        else if(AppControllerUtil.getInstance().getCurrent_fragment().equals("ReminderList")){
            fragment=new MrActions();

        }
        else if(AppControllerUtil.getInstance().getCurrent_fragment().equals("TakeSurvey")){
            fragment=new MrActions();

        }
        else if(AppControllerUtil.getInstance().getCurrent_fragment().equals("SaveSignature")){
            fragment=new MrActions();

        }
        else if(AppControllerUtil.getInstance().getCurrent_fragment().equals("MonthlyVisit")){
            fragment=new TodayVisit();
            setTitle("Today Visit");

        }
        else if(AppControllerUtil.getInstance().getCurrent_fragment().equals("ClientList")){
            fragment=new MonthlyVisit();
            setTitle("Monthly Visit");

        }
        else if(AppControllerUtil.getInstance().getCurrent_fragment().equals("RoutePlan")){
            fragment=new TodayVisit();
            setTitle("Today Visit");

        }
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }
    private void setTitle(String title){
        toolbar.setTitle(title);
    }

}
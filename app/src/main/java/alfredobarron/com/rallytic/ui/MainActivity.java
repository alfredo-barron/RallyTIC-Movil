package alfredobarron.com.rallytic.ui;

import alfredobarron.com.rallytic.R;
import alfredobarron.com.rallytic.listener.OnMakeSnackbar;
import alfredobarron.com.rallytic.resources.ConnectionDetector;
import alfredobarron.com.rallytic.resources.SessionManager;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static butterknife.ButterKnife.findById;

public class MainActivity extends AppCompatActivity implements OnMakeSnackbar {

    @Bind(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    TextView title;

    private ActionBarDrawerToggle mDrawerToggle;

    private int mCurrentMenu;

    private EventFragment mEventFragment;

    private ActivitiesFragment mActivitiesFragment;

    private TeamFragment mTeamFragment;

    static SessionManager sessionManager;

    ConnectionDetector cd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(getApplication());
        if(sessionManager.checkLogin() == true)
            finish();
        ButterKnife.bind(this);
        Toolbar toolbar = findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        setupNavigationView(toolbar);
        switchFragment(R.id.nav_home);
        cd = new ConnectionDetector(this);
        title = (TextView) findViewById(R.id.team_name);
        title.setText(sessionManager.obtenerUser());
    }

    private void switchFragment(int menuId) {
        mCurrentMenu = menuId;
        ActionBar actionBar = getSupportActionBar();
        switch (menuId) {
            case R.id.nav_home:
                if (actionBar != null) {
                    actionBar.setTitle("Evento");
                }
                if (mEventFragment == null) {
                    mEventFragment = EventFragment.newInstance();
                }
                replaceMainFragment(mEventFragment);
                break;
            case R.id.nav_act:
                if (actionBar != null) {
                    actionBar.setTitle("Actividad");
                }
                if (mActivitiesFragment == null) {
                    mActivitiesFragment = ActivitiesFragment.newInstance();
                }
                replaceMainFragment(mActivitiesFragment);
                //startActivity(new Intent(this, RequestActivity.class));
                break;
            case R.id.nav_map:
                startActivity(new Intent(this, MapActivity.class));
                break;
            case R.id.nav_team:
                if (actionBar != null) {
                    actionBar.setTitle("Equipo");
                }
                if (mTeamFragment == null) {
                    mTeamFragment = TeamFragment.newInstance();
                }
                replaceMainFragment(mTeamFragment);
                break;
            case R.id.nav_out:
                sessionManager.logoutUser();
                finish();
            default:
                break;
        }
    }

    private void setupNavigationView(Toolbar toolbar) {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        NavigationView navigationView = findById(this, R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switchFragment(menuItem.getItemId());
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void replaceMainFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public Snackbar onMakeSnackbar(CharSequence text, int duration) {
        return Snackbar.make(mCoordinatorLayout, text, duration);
    }
}




package ru.nzif.mayak_meter;

import android.content.Intent;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import java.lang.reflect.Field;
import java.util.ArrayList;

import ru.nzif.mayak_meter.utils.DialogsManager;
import ru.nzif.mayak_meter.utils.GlobalUtils;
import ru.nzif.mayak_meter.utils.PreferencesManager;
import ru.nzif.mayak_meter.view.controler.SlidingMenu;

public class ActivityMain extends AppCompatActivity implements SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemLongClickListener {
    private static final int MENU_ITEM_ANIMATION_DURATION = 850;
    private static final String TOOLBAR_REMOVE_MODE_SPACES = "     ";

    private static final String TOOLBAR_TEXTVIEW_FIELD_NAME = "mTitleTextView";
    private static final String TOOLBAR_NAV_BTN_FIELD_NAME = "mNavButtonView";

    private TextView tvToolbarTitle;
    private ImageButton btnToolbarButton;
    private TextView tvNoInternet;

    public static enum Mode {
        NORMAL, REMOVE, SEARCH, SORT;
    }
    public static Mode mode;

    private Toolbar mActionBarToolbar;
    private SearchView mSearchView;
    private SlidingMenu slidingMenu;

    private MenuItem editMenuItem;
    private MenuItem microMenuItem;
    private MenuItem removeMenuItem;
    private MenuItem searchMenuItem;
    private MenuItem sortAbMenuItem;

    private ProgressBarCircularIndeterminate pbLoadingQuotes;

    private DynamicListView lvMainListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // -- Init
        PreferencesManager.getInstance().init(getApplicationContext());
        mode = Mode.NORMAL;

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);

        this.slidingMenu = new SlidingMenu(this);
        /*try {
            Field f = mActionBarToolbar.getClass().getDeclaredField(TOOLBAR_TEXTVIEW_FIELD_NAME);
            f.setAccessible(true);
            tvToolbarTitle = (TextView) f.get(mActionBarToolbar);

            f = mActionBarToolbar.getClass().getDeclaredField(TOOLBAR_NAV_BTN_FIELD_NAME);
            f.setAccessible(true);
            btnToolbarButton = (ImageButton) f.get(mActionBarToolbar);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        populateMainListview();
        DialogsManager.showWelcomeDialog(this);*/
    }

    private void populateMainListview() {
        pbLoadingQuotes.setVisibility(View.GONE);
        tvNoInternet.setVisibility(View.VISIBLE);
        return;
        /*
        if (!GlobalUtils.isNetworkConnected(this)) {
            pbLoadingQuotes.setVisibility(View.GONE);
            tvNoInternet.setVisibility(View.VISIBLE);
            return;
        } else if (tvNoInternet.getVisibility() == View.VISIBLE) {
            tvNoInternet.setVisibility(View.GONE);
        }

        if (lvMainListview == null) {
            lvMainListview = (DynamicListView) findViewById(R.id.lvFinanceItemsList);
            lvMainListview.enableDragAndDrop();
            lvMainListview.setOnItemClickListener(this);
            lvMainListview.setOnItemLongClickListener(this);
        }*/
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        microMenuItem = menu.findItem(R.id.action_micro);
        removeMenuItem = menu.findItem(R.id.action_remove);
        searchMenuItem = menu.findItem(R.id.action_search);
        editMenuItem = menu.findItem(R.id.action_edit);
        sortAbMenuItem = menu.findItem(R.id.action_sort);
        mSearchView = (SearchView) searchMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, this);
        startMode(Mode.NORMAL);
        return true;
    }

    @Override
    public void onRefresh() {
        populateMainListview();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
        return;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String queryText) {

        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        startMode(Mode.NORMAL);
        populateMainListview();
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        slidingMenu.mDrawerToggle.syncState();
    }

    private void startMode(Mode modeToStart) {
        if (mode == Mode.SORT) {
            //Save order before exit sort mode

        } else if (mode == Mode.REMOVE) {
            //Clear listview cache after exiting form remove mode.
            lvMainListview.invalidate();
        }
        if (modeToStart == Mode.NORMAL) {
            removeMenuItem.setVisible(false);
            microMenuItem.setVisible(false);
            sortAbMenuItem.setVisible(false);
            searchMenuItem.setVisible(true);
            editMenuItem.setVisible(true);
            //mActionBarToolbar.setLogo(null);
            //mActionBarToolbar.setTitle(getString(R.string.app_name));
            //mActionBarToolbar.setBackgroundResource(R.color.toolbar_green);
            slidingMenu.mDrawerToggle.setDrawerIndicatorEnabled(true);
            slidingMenu.mDrawerLayout.setEnabled(true);
            slidingMenu.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            slidingMenu.mDrawerToggle.setHomeAsUpIndicator(null);
            if (mode != modeToStart) {
                GlobalUtils.safeAnimate(findViewById(searchMenuItem.getItemId()), MENU_ITEM_ANIMATION_DURATION, Techniques.FlipInX);
                GlobalUtils.safeAnimate(findViewById(editMenuItem.getItemId()), MENU_ITEM_ANIMATION_DURATION, Techniques.FlipInX);
                GlobalUtils.safeAnimate(tvToolbarTitle, MENU_ITEM_ANIMATION_DURATION, Techniques.FlipInX);
                GlobalUtils.safeAnimate(btnToolbarButton, MENU_ITEM_ANIMATION_DURATION, Techniques.FlipInX);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.status_bar_orange));
            }

            mode = modeToStart;
            return;
        } else if (modeToStart == Mode.SORT) {
            removeMenuItem.setVisible(false);
            searchMenuItem.setVisible(false);
            editMenuItem.setVisible(false);
            microMenuItem.setVisible(false);
            sortAbMenuItem.setVisible(true);
            //mActionBarToolbar.setTitle(getString(R.string.drag_drop));
            GlobalUtils.safeAnimate(findViewById(sortAbMenuItem.getItemId()), MENU_ITEM_ANIMATION_DURATION, Techniques.FlipInX);
            GlobalUtils.safeAnimate(tvToolbarTitle, MENU_ITEM_ANIMATION_DURATION, Techniques.FlipInX);
            //mActionBarToolbar.setBackgroundResource(R.color.price_green);
            //mActionBarToolbar.setLogo(null);
            slidingMenu.mDrawerToggle.setHomeAsUpIndicator(R.drawable.abc_tab_indicator_material);
            slidingMenu.mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startMode(Mode.NORMAL);
                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.status_bar_green));
            }
        } else if (modeToStart == Mode.REMOVE) {
            searchMenuItem.setVisible(false);
            editMenuItem.setVisible(false);
            microMenuItem.setVisible(false);
            removeMenuItem.setVisible(true);
            GlobalUtils.safeAnimate(findViewById(removeMenuItem.getItemId()), MENU_ITEM_ANIMATION_DURATION, Techniques.FlipInX);
            //mActionBarToolbar.setLogo(R.drawable.icon_toolbar_checked);
            //mActionBarToolbar.setTitle(TOOLBAR_REMOVE_MODE_SPACES + "0 " + getString(R.string.from) + " ");
            GlobalUtils.safeAnimate(tvToolbarTitle, MENU_ITEM_ANIMATION_DURATION, Techniques.FlipInX);
            GlobalUtils.safeAnimate(btnToolbarButton, MENU_ITEM_ANIMATION_DURATION, Techniques.FlipInX);
            //mActionBarToolbar.setBackgroundResource(R.color.price_red);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.status_bar_red));
            }
        } else if (modeToStart == Mode.SEARCH) {
            removeMenuItem.setVisible(false);
            searchMenuItem.setVisible(false);
            editMenuItem.setVisible(false);
            microMenuItem.setVisible(true);
        }
        mode = modeToStart;

        slidingMenu.mDrawerToggle.setDrawerIndicatorEnabled(false);
        slidingMenu.mDrawerLayout.setEnabled(false);
        slidingMenu.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
}
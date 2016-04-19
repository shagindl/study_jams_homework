package ru.nzif.mayak_meter;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import java.lang.reflect.Field;
import java.util.ArrayList;

import ru.nzif.mayak_meter.utils.DialogsManager;
import ru.nzif.mayak_meter.utils.GlobalUtils;
import ru.nzif.mayak_meter.utils.PreferencesManager;

public class ActivityMain extends AppCompatActivity implements SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemLongClickListener {
    private static final String TOOLBAR_TEXTVIEW_FIELD_NAME = "mTitleTextView";
    private static final String TOOLBAR_NAV_BTN_FIELD_NAME = "mNavButtonView";

    private TextView tvToolbarTitle;
    private ImageButton btnToolbarButton;
    private TextView tvNoInternet;

    private Toolbar mActionBarToolbar;

    private ProgressBarCircularIndeterminate pbLoadingQuotes;

    private DynamicListView lvMainListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);
        try {
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
        DialogsManager.showWelcomeDialog(this);
    }

    private void populateMainListview() {
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
        }
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
    public boolean onMenuItemActionCollapse(MenuItem item) {

        populateMainListview();
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
}
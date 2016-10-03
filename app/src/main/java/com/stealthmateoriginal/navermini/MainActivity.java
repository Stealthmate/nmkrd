package com.stealthmateoriginal.navermini;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.stealthmateoriginal.navermini.UI.CustomViewPager;
import com.stealthmateoriginal.navermini.state.ResultListDictionary;
import com.stealthmateoriginal.navermini.state.StateManager;

public class MainActivity extends FragmentActivity {

    private StateManager state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.state = new StateManager(this);

        CustomViewPager pager = (CustomViewPager) findViewById(R.id.viewpager);
        pager.initialize(state);
        this.pager = pager;

        RadioButton btn_kr = (RadioButton) findViewById(R.id.view_home_dict_btn_kr);
        btn_kr.setChecked(true);

        LinearLayout container = (LinearLayout) findViewById(R.id.view_main_container);
        container.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        final LinearLayout sidebar = (LinearLayout) findViewById(R.id.view_main_sidebar);
        ImageButton topbar = (ImageButton) findViewById(R.id.view_main_menu_btn);
        topbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) sidebar.getLayoutParams();
                if(params.width != 0) params.width = 0;
                else params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
                sidebar.setLayoutParams(params);
            }
        });

    }

    private ViewPager pager;


    public StateManager getState() {
        return state;
    }


    public void goToDetails() {
        ((CustomViewPager) findViewById(R.id.viewpager)).setCurrentItem(1);
    }

    public void goToSearch() {
        ((CustomViewPager) findViewById(R.id.viewpager)).setCurrentItem(0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // TODO Auto-generated method stub
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            if(pager.getCurrentItem() == 1) {
                goToSearch();
            }
            else {
                return super.onKeyDown(keyCode, event);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public boolean onSelectDictionary(View v) {
        switch(v.getId()) {
            case R.id.view_home_dict_btn_kr: {
                state.setCurrentDictionary(ResultListDictionary.KOREAN);
            } break;
            case R.id.view_home_dict_btn_jp: {
                state.setCurrentDictionary(ResultListDictionary.JAPANESE);
            }
        }
        state.getSearchFragment().setSubDictionaryList(state.getCurrentDictionary());
        return true;
    }
}

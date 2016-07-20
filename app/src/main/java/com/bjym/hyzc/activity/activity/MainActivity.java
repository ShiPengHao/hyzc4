package com.bjym.hyzc.activity.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bjym.hyzc.R;
import com.bjym.hyzc.activity.bean.Myself;
import com.bjym.hyzc.activity.fragment.AccenterFragment;
import com.bjym.hyzc.activity.fragment.DiaoChaFragment;
import com.bjym.hyzc.activity.fragment.HomeFragment;
import com.bjym.hyzc.activity.fragment.TongJiFragment;
import com.bjym.hyzc.activity.utils.MyConstant;
import com.bjym.hyzc.activity.utils.MyToast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private TextView tv_my;
    private TextView tv_diaocha;
    private TextView tv_home;
    private TextView tv_tongji;
    private HomeFragment home;
    private DiaoChaFragment diaoCha;
    private AccenterFragment accenter;
    private TongJiFragment tongJi;

    private TextView tv_menu_accout;
    private TextView tv_menu_keshi;

    private FragmentManager manager;
    private DrawerLayout drawer;

    private List<Myself> myselefLists;
    private static String departmentCode;
    private static String realName;
    private Bundle bundle;
    private String userCode;

    @Override
    public View setMainView() {
        View view = View.inflate(context, R.layout.activity_main, null);

        Toolbar toolbar = initView(view);

        defaultHomeView();
        defaultReplaceHomeFragment();
        dealFloatingButton(view);

        dealDrawerLayout(view, toolbar);

        dealNavigation(view);

        return view;
    }

    private void dealNavigation(View view) {
        NavigationView navigationView = (NavigationView) view.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

       /*
       *  在这里处理菜单头布局的逻辑
       */

        View headerView = navigationView.getHeaderView(0);
        tv_menu_keshi = (TextView) headerView.findViewById(R.id.tv_menu_keshi);
        tv_menu_accout = (TextView) headerView.findViewById(R.id.tv_menu_accout);
    }


    @Override
    public void InitData() {
        tv_my.setOnClickListener(this);
        tv_diaocha.setOnClickListener(this);
        tv_tongji.setOnClickListener(this);
        tv_home.setOnClickListener(this);
        getNetData();
    }

    private Toolbar initView(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_my = (TextView) view.findViewById(R.id.tv_accountcenter);
        tv_home = (TextView) view.findViewById(R.id.tv_home);
        tv_diaocha = (TextView) view.findViewById(R.id.tv_diaocha);
        tv_tongji = (TextView) view.findViewById(R.id.tv_tongji);
        return toolbar;
    }

    private void dealDrawerLayout(View view, Toolbar toolbar) {
        drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void dealFloatingButton(View view) {
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void defaultReplaceHomeFragment() {
        //默认填充主页fragment
        manager = getSupportFragmentManager();
        FragmentTransaction transition = manager.beginTransaction();
        if (home == null) {
            home = new HomeFragment();

        }
        transition.replace(R.id.ll_content, home).commit();
    }


    private void defaultHomeView() {
        //默认主页为绿色
        Drawable home2 = getResources().getDrawable(R.mipmap.home2);
        home2.setBounds(0, 0, home2.getMinimumWidth(), home2.getMinimumHeight());
        tv_home.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv_home.setCompoundDrawables(null, home2, null, null);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        FragmentTransaction transaction = manager.beginTransaction();
        switch (v.getId()) {
            case R.id.tv_accountcenter:
                if (accenter == null) {
                    accenter = new AccenterFragment();
                    accenter.setArguments(bundle);

                }
                resetMyView();
                transaction.replace(R.id.ll_content, accenter, "AccenterFragment");
                break;
            case R.id.tv_diaocha:
                if (diaoCha == null) {
                    diaoCha = new DiaoChaFragment();
                    diaoCha.setArguments(bundle);
                }
                resetDiaoChaView();
                transaction.replace(R.id.ll_content, diaoCha, "DiaoChaFragment");
                break;
            case R.id.tv_home:
                if (home == null) {
                    home = new HomeFragment();
                }
                resetHomeView();
                transaction.replace(R.id.ll_content, home, "HomeFragment");
                break;
            case R.id.tv_tongji:
                if (tongJi == null) {
                    tongJi = new TongJiFragment();
                }
                resetTongJiView();
                transaction.replace(R.id.ll_content, tongJi, "TongJiFragment");
                break;
            default:
                break;
        }
        transaction.commit();

    }


    @Override
    public void onBackPressed() {
        //   DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /*
    * 1.得到用户信息设置菜单页面的用户名
    * 2.传递给accountfragment 和 diaochaFragment
    * */
    private void getNetData() {
        OkHttpUtils.get().url(MyConstant.MYMSG_URL).build().execute(new Callback() {

            @Override
            public Object parseNetworkResponse(Response response, int i) throws Exception {
                String json = response.body().string();
                parseMyselfJson(json);
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int i) {
                MyToast.showToast(MainActivity.this, "请求失败");
            }

            @Override
            public void onResponse(Object o, int in) {
                for (int i = 0; i < myselefLists.size(); i++) {
                    Myself myself = myselefLists.get(i);
                    realName = myself.RealName;
                    departmentCode = myself.DepartmentCode;
                    userCode = myself.UserCode;

                }

                tv_menu_accout.setText("用户名：" + userCode);
                tv_menu_keshi.setText("科室名：" + departmentCode);

                /*
                * 通过Bundle给acconteFragment和diaochaFragment传值
                * */
                bundle = getBundle();


            }
        });
    }

    @NonNull
    private Bundle getBundle() {
        Bundle bundle = new Bundle();

        bundle.putString("departmentCode", departmentCode);
        bundle.putString("realName", realName);
        bundle.putString("userCode", userCode);
        return bundle;
    }

    private void parseMyselfJson(String json) {

        myselefLists = new Gson().fromJson(json, new TypeToken<List<Myself>>() {
        }.getType());
    }

    private void resetHomeView() {

        //默认主页为绿色
        defaultHomeView();

        //tv_diaocha为灰色
        Drawable diaocha = getResources().getDrawable(R.mipmap.diaocha);
        diaocha.setBounds(0, 0, diaocha.getMinimumWidth(), diaocha.getMinimumHeight());
        tv_diaocha.setTextColor(getResources().getColor(R.color.Hui));
        tv_diaocha.setCompoundDrawables(null, diaocha, null, null);

        //tv_tongji为灰色
        Drawable tongji = getResources().getDrawable(R.mipmap.tongji);
        tongji.setBounds(0, 0, tongji.getMinimumWidth(), tongji.getMinimumHeight());
        tv_tongji.setTextColor(getResources().getColor(R.color.Hui));
        tv_tongji.setCompoundDrawables(null, tongji, null, null);


        //tv_my为灰色
        Drawable my = getResources().getDrawable(R.mipmap.my);
        my.setBounds(0, 0, my.getMinimumWidth(), my.getMinimumHeight());
        tv_my.setTextColor(getResources().getColor(R.color.Hui));
        tv_my.setCompoundDrawables(null, my, null, null);
    }

    private void resetDiaoChaView() {
        //默认主页为绿色
        Drawable diaocha2 = getResources().getDrawable(R.mipmap.diaocha2);
        diaocha2.setBounds(0, 0, diaocha2.getMinimumWidth(), diaocha2.getMinimumHeight());
        tv_diaocha.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv_diaocha.setCompoundDrawables(null, diaocha2, null, null);

        //tv_diaocha为灰色
        Drawable home = getResources().getDrawable(R.mipmap.home);
        home.setBounds(0, 0, home.getMinimumWidth(), home.getMinimumHeight());
        tv_home.setTextColor(getResources().getColor(R.color.Hui));
        tv_home.setCompoundDrawables(null, home, null, null);

        //tv_tongji为灰色
        Drawable tongji = getResources().getDrawable(R.mipmap.tongji);
        tongji.setBounds(0, 0, tongji.getMinimumWidth(), tongji.getMinimumHeight());
        tv_tongji.setTextColor(getResources().getColor(R.color.Hui));
        tv_tongji.setCompoundDrawables(null, tongji, null, null);


        //tv_my为灰色
        Drawable my = getResources().getDrawable(R.mipmap.my);
        my.setBounds(0, 0, my.getMinimumWidth(), my.getMinimumHeight());
        tv_my.setTextColor(getResources().getColor(R.color.Hui));
        tv_my.setCompoundDrawables(null, my, null, null);

    }

    private void resetMyView() {

        //默认主页为绿色
        Drawable my2 = getResources().getDrawable(R.mipmap.my2);
        my2.setBounds(0, 0, my2.getMinimumWidth(), my2.getMinimumHeight());
        tv_my.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv_my.setCompoundDrawables(null, my2, null, null);

        //tv_diaocha为灰色
        Drawable home = getResources().getDrawable(R.mipmap.home);
        home.setBounds(0, 0, home.getMinimumWidth(), home.getMinimumHeight());
        tv_home.setTextColor(getResources().getColor(R.color.Hui));
        tv_home.setCompoundDrawables(null, home, null, null);

        //tv_tongji为灰色
        Drawable tongji = getResources().getDrawable(R.mipmap.tongji);
        tongji.setBounds(0, 0, tongji.getMinimumWidth(), tongji.getMinimumHeight());
        tv_tongji.setTextColor(getResources().getColor(R.color.Hui));
        tv_tongji.setCompoundDrawables(null, tongji, null, null);


        //tv_diaocha为灰色
        Drawable diaocha = getResources().getDrawable(R.mipmap.diaocha);
        diaocha.setBounds(0, 0, diaocha.getMinimumWidth(), diaocha.getMinimumHeight());
        tv_diaocha.setTextColor(getResources().getColor(R.color.Hui));
        tv_diaocha.setCompoundDrawables(null, diaocha, null, null);

    }


    private void resetTongJiView() {
        //默认主页为绿色
        Drawable tongji2 = getResources().getDrawable(R.mipmap.tongji2);
        tongji2.setBounds(0, 0, tongji2.getMinimumWidth(), tongji2.getMinimumHeight());
        tv_tongji.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv_tongji.setCompoundDrawables(null, tongji2, null, null);

        //tv_diaocha为灰色
        Drawable home = getResources().getDrawable(R.mipmap.home);
        home.setBounds(0, 0, home.getMinimumWidth(), home.getMinimumHeight());
        tv_home.setTextColor(getResources().getColor(R.color.Hui));
        tv_home.setCompoundDrawables(null, home, null, null);

        //tv_my为灰色
        Drawable my = getResources().getDrawable(R.mipmap.my);
        my.setBounds(0, 0, my.getMinimumWidth(), my.getMinimumHeight());
        tv_my.setTextColor(getResources().getColor(R.color.Hui));
        tv_my.setCompoundDrawables(null, my, null, null);


        //tv_diaocha为灰色
        Drawable diaocha = getResources().getDrawable(R.mipmap.diaocha);
        diaocha.setBounds(0, 0, diaocha.getMinimumWidth(), diaocha.getMinimumHeight());
        tv_diaocha.setTextColor(getResources().getColor(R.color.Hui));
        tv_diaocha.setCompoundDrawables(null, diaocha, null, null);

    }


}

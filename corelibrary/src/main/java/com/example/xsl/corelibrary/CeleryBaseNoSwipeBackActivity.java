package com.example.xsl.corelibrary;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xsl.corelibrary.utils.CoreLibraryRetriever;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @anthor xieshuilin
 * @date 2019/08/16
 * 不需要右滑动的BaseActivity
 */
public class CeleryBaseNoSwipeBackActivity extends AppCompatActivity implements LifecycleProvider<ActivityEvent> {


    private LinearLayout celeryLayout;
    private ViewStub viewStub;
    protected AppBarLayout appBarLayout;
    protected Toolbar toolbar;
    protected RelativeLayout container_rl;
    protected TextView navTitleText,tvAction;
    protected ImageView imageAction,imageAction2;
    protected Activity mContext;
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext =this;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.celery_base_activity_layout);
        celeryLayout = (LinearLayout) findViewById(R.id.celery_layout);
        viewStub = (ViewStub) findViewById(R.id.view_stub);
        View view = getLayoutInflater().inflate(layoutResID, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        celeryLayout.addView(view,layoutParams);
    }



    /**
     * 初始化ToolBar
     * 懒加载方式
     */
    protected void toolBarInit(){
        if (viewStub != null && appBarLayout == null){
            appBarLayout = (AppBarLayout) viewStub.inflate();
            toolbar = (Toolbar) appBarLayout.findViewById(R.id.toolbar);
            navTitleText = toolbar.findViewById(R.id.mTitle);
            container_rl = toolbar.findViewById(R.id.container_rl);
            tvAction = toolbar.findViewById(R.id.tvAction);
            imageAction = toolbar.findViewById(R.id.imageAction);
            imageAction2 = toolbar.findViewById(R.id.imageAction2);
            toolbar.setNavigationIcon(R.drawable.nav_icon);
            toolbar.setTitle("");
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            navTitleText.setTextColor(getResources().getColor(R.color.white));
            tvAction.setTextColor(getResources().getColor(R.color.white));
            setSupportActionBar(toolbar);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.hide();
    }
    @Override
    public final <T> LifecycleTransformer<T> bindUntilEvent(ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }
    @Override
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }
    @Override
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }
    @Override
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }
    @Override
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
    }

}

package com.conicskill.app.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsCallback;
import androidx.lifecycle.ViewModelProvider;

import com.conicskill.app.BuildConfig;
import com.conicskill.app.R;
import com.conicskill.app.base.BaseActivity;
import com.conicskill.app.data.model.candidateLogin.CandidateLogOut;
import com.conicskill.app.database.DatabaseConstant;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.ui.home.AboutUsFragment;
import com.conicskill.app.ui.home.HomeNavigationBar;
import com.conicskill.app.ui.home.MockTestFragment;
import com.conicskill.app.ui.home.PrivacyPolicyFragment;
import com.conicskill.app.ui.home.RefundPolicyFragment;
import com.conicskill.app.ui.home.SupportFragment;
import com.conicskill.app.ui.home.TermsAndConditionsFragment;
import com.conicskill.app.ui.login.LoginViewModel;
import com.conicskill.app.util.CommonView;
import com.conicskill.app.util.Constants;
import com.conicskill.app.util.FontHelper;
import com.conicskill.app.util.TinyDB;
import com.conicskill.app.util.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.androidbrowserhelper.trusted.QualityEnforcer;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableBadgeDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;


public class HomeActivity extends BaseActivity {

    private static final int PROFILE_SETTING = 100000;
    @Inject
    ViewModelFactory viewModelFactory;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bottomNavigationHome)
    BottomNavigationView bottomNavigationView;
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private HomeNavigationBar homeFragment;
    private TinyDB tinyDB;
    private LoginViewModel mLoginViewModel;
    private final String SENDER_ID = "20727129680";

    @Override
    protected int layoutRes() {
        return R.layout.home_activity;
    }

    private CustomTabsCallback mCustomTabsCallback = new QualityEnforcer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (savedInstanceState == null) {
            if (homeFragment == null) {
                homeFragment = new HomeNavigationBar();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.screenContainer, homeFragment).commit();
//        }
        tinyDB = new TinyDB(getApplicationContext());
        mLoginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.inflateMenu(R.menu.header_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.share) {
                    /*Create an ACTION_SEND Intent*/
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    /*This will be the actual content you wish you share.*/
                    String shareBody = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                    /*The type of the content is text, obviously.*/
                    intent.setType("text/plain");
                    /*Applying information Subject and Body.*/
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Download the App from Play Store: ");
                    intent.putExtra(Intent.EXTRA_TEXT, "Download the App from Play Store: \n\n\n"
                            + shareBody);
                    /*Fire!*/
                    startActivity(Intent.createChooser(intent, "Share using..."));
                } else if (item.getItemId() == R.id.notification) {

                }
                return false;
            }
        });
        initSetDrawer(savedInstanceState);
        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);

        new Thread(() -> {
            try {
                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
                    String mToken = instanceIdResult.getToken();
                    FirebaseMessaging.getInstance().subscribeToTopic(BuildConfig.COMPANY_ID);
                });
            } catch (Exception e) {

            }
        }).start();
        observeLogOut();

        /*bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.home:
                            getSupportFragmentManager().beginTransaction().replace(R.id.screenContainer, homeFragment).commit();
                            break;
                        case R.id.videoClasses:
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.screenContainer, VideoCourseListingFragment.newInstance())
                                    .commit();
                            break;
                        case R.id.ourTest:
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.screenContainer, MockTestFragment.newInstance(null, "live"))
                                    .commit();
                            break;
                        case R.id.liveTest:
                            *//*getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.screenContainer, MockTestFragment.newInstance("1", "live"))
                                    .commit();*//*
                            break;
                    }
                    return true;
                });*/
    }

    private void observeLogOut() {
        mLoginViewModel.observeLogOut().observe(this, result-> {
            if(result != null) {
                assert result.body() != null;
                if(result.body().getCode()!= null &&  result.body().getCode() == 1000) {
                    Utils.deleteDatabaseFile(getApplicationContext(), DatabaseConstant.DATABASE_NAME);
                    tinyDB.putString(Constants.NAME, "");
                    tinyDB.putString(Constants.EMAIL, "");
                    tinyDB.putBoolean(Constants.IS_LOGGED_IN, false);
                    Intent intent = new Intent(HomeActivity.this, SplashActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void initSetDrawer(Bundle savedInstanceState) {
        // Create a few sample profile
        // NOTE you have to define the loader_ecg logic too. See the CustomApplication for more details
        final IProfile profile = new ProfileDrawerItem().withTypeface(FontHelper.getInstance(getApplicationContext()).getAppCustomTypeface())
                .withTextColorRes(R.color.md_white_1000).withName(tinyDB.getString(Constants.NAME)).withTextColorRes(R.color.md_white_1000)
                .withEmail(tinyDB.getString(Constants.NAME)).withTextColorRes(R.color.md_white_1000)
                .withIcon("https://www.google.com/url?sa=i&source=images&cd=&ved=2ahUKEwiIzuqqmZXnAhXCAnIKHVfiAxgQjRx6BAgBEAQ&url=https%3A%2F%2Fwww.rd.com%2Fadvice%2Frelationships%2Fonline-dating-profile-photos%2F&psig=AOvVaw1fsMKM7w6h_IAHMGUWxGwE&ust=1579713451935250").withIdentifier(100);

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTextColorRes(R.color.md_white_1000)
                .withTranslucentStatusBar(true)
                .withEmailTypeface(FontHelper.getInstance(getApplicationContext()).getAppCustomTypeface())
                .withNameTypeface(FontHelper.getInstance(getApplicationContext()).getAppCustomTypeface())
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        //sample usage of the onProfileChanged listener
                        //if the clicked item has the identifier 1 add a new profile ;)
                        if (profile instanceof IDrawerItem && profile.getIdentifier() == PROFILE_SETTING) {
                            int count = 100 + headerResult.getProfiles().size() + 1;
                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman" + count).withEmail("batman" + count + "@gmail.com").withIcon("https://avatars3.githubusercontent.com/u/1476232?v=3&s=460").withIdentifier(count).withTextColorRes(R.color.md_white_1000);
                            if (headerResult.getProfiles() != null) {
                                //we know that there are 2 setting elements. set the new profile above them ;)
                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
                            } else {
                                headerResult.addProfiles(newProfile);
                            }
                        }

                        //false if you have not consumed the event and it should close the drawer
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withSliderBackgroundColorRes(R.color.colorPrimaryDarkNew)
                .withHasStableIds(true)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withTypeface(FontHelper.getInstance(getApplicationContext()).getAppCustomTypeface())
                                .withName(R.string.text_dashboard).withIdentifier(1).withSelectable(true).withTextColorRes(R.color.md_white_1000)
                                .withIconTintingEnabled(true).withIcon(R.drawable.ic_dashboard_white_24dp),
                        new ExpandableBadgeDrawerItem().withTypeface(FontHelper.getInstance(getApplicationContext()).getAppCustomTypeface())
                                .withName(R.string.text_exams_tests).withIdentifier(18).withSelectable(false)
                                .withTextColorRes(R.color.md_white_1000).withIconTintingEnabled(true)
                                .withIcon(R.drawable.ic_education_white_24dp).withSubItems(
                                new PrimaryDrawerItem().withTypeface(FontHelper.getInstance(getApplicationContext()).getAppCustomTypeface())
                                        .withName(R.string.text_mock_test).withLevel(2).withIdentifier(2000).
                                        withTextColorRes(R.color.md_white_1000).
                                        withIcon(R.drawable.ic_library_books_white_24dp).withIconTintingEnabled(true),
                                new PrimaryDrawerItem().withTypeface(FontHelper.getInstance(getApplicationContext()).getAppCustomTypeface())
                                        .withName(R.string.text_live_tests).withLevel(2).withIdentifier(2001).
                                        withTextColorRes(R.color.md_white_1000).
                                        withIcon(R.drawable.ic_local_library_white_24dp).withIconTintingEnabled(true),
                                /*new PrimaryDrawerItem().withTypeface(FontHelper.getInstance(getApplicationContext()).getAppCustomTypeface())
                                        .withName(R.string.text_topic_tests).withLevel(2).withIdentifier(2002).
                                        withTextColorRes(R.color.md_white_1000).
                                        withIcon(R.drawable.ic_aspect_ratio_white_24dp).withIconTintingEnabled(true),*/
                                new PrimaryDrawerItem().withTypeface(FontHelper.getInstance(getApplicationContext()).getAppCustomTypeface())
                                        .withName(R.string.text_daily_quizzes).withLevel(2).withIdentifier(2003).
                                        withTextColorRes(R.color.md_white_1000).
                                        withIcon(R.drawable.ic_today_white_24dp).withIconTintingEnabled(true)

                        ),
                        new PrimaryDrawerItem().withTypeface(FontHelper.getInstance(getApplicationContext()).getAppCustomTypeface()).withIconTintingEnabled(true).withIcon(R.drawable.ic_notifications_white_24dp)
                                .withName(R.string.text_notifications).withIdentifier(4).withSelectable(true).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_400)).withTextColorRes(R.color.md_white_1000),
                        new PrimaryDrawerItem().withTypeface(FontHelper.getInstance(getApplicationContext()).getAppCustomTypeface()).withIconTintingEnabled(true).withIcon(R.drawable.ic_outline_assessment_24px)
                                .withName(R.string.text_insights_trends).withIdentifier(60).withSelectable(true).withTextColorRes(R.color.md_white_1000)/*,
                        new PrimaryDrawerItem().withTypeface(FontHelper.getInstance(getApplicationContext()).getAppCustomTypeface()).withIconTintingEnabled(true).withIcon(R.drawable.ic_explore_white_24dp)
                                .withName(R.string.text_getting_started).withIdentifier(5).withSelectable(true).withTextColorRes(R.color.md_white_1000)*/,/*
                        new PrimaryDrawerItem().withTypeface(FontHelper.getInstance(getApplicationContext()).getAppCustomTypeface()).withIconTintingEnabled(true).withIcon(R.drawable.ic_education_white_24dp)
                                .withName(R.string.text_education).withIdentifier(50).withSelectable(true).withTextColorRes(R.color.md_white_1000),*/
                        new DividerDrawerItem(),/*
                        new SecondaryDrawerItem().withTypeface(FontHelper.getInstance(getApplicationContext()).getAppCustomTypeface()).withIconTintingEnabled(true).withIcon(R.drawable.ic_settings_white_24dp)
                                .withName(R.string.text_settings).withIdentifier(11).withSelectable(true).withTextColorRes(R.color.md_white_1000),*/
                        new SecondaryDrawerItem().withTypeface(FontHelper.getInstance(getApplicationContext()).getAppCustomTypeface())
                                .withName(R.string.text_contact_us).withIdentifier(20).withSelectable(true).withIcon(R.drawable.ic_contacts_white_24dp).withTextColorRes(R.color.md_white_1000),
                        new SecondaryDrawerItem().withTypeface(FontHelper.getInstance(getApplicationContext()).getAppCustomTypeface())
                                .withName(R.string.text_about_us).withIdentifier(25).withSelectable(true).withIcon(R.drawable.ic_info_outline_white_24dp).withTextColorRes(R.color.md_white_1000),
                        new SecondaryDrawerItem().withTypeface(FontHelper.getInstance(getApplicationContext()).getAppCustomTypeface())
                                .withName(R.string.text_profile).withIdentifier(21).withSelectable(true).withIcon(R.drawable.ic_profile_white_24dp).withTextColorRes(R.color.md_white_1000),

                        new SecondaryDrawerItem().withTypeface(FontHelper.getInstance(getApplicationContext()).getAppCustomTypeface())
                                .withName(R.string.txt_privacy_policy).withIdentifier(500).withSelectable(true).withIcon(R.drawable.ic_baseline_privacy_policy_24).withTextColorRes(R.color.md_white_1000),
                        new SecondaryDrawerItem().withTypeface(FontHelper.getInstance(getApplicationContext()).getAppCustomTypeface())
                                .withName(R.string.txt_refund_policy).withIdentifier(501).withSelectable(true).withIcon(R.drawable.ic_baseline_privacy_policy_24).withTextColorRes(R.color.md_white_1000),
                        new SecondaryDrawerItem().withTypeface(FontHelper.getInstance(getApplicationContext()).getAppCustomTypeface())
                                .withName(R.string.txt_terms).withIdentifier(502).withSelectable(true).withIcon(R.drawable.ic_baseline_privacy_policy_24).withTextColorRes(R.color.md_white_1000),


                        /*new SecondaryDrawerItem().withTypeface(FontHelper.getInstance(getApplicationContext()).getAppCustomTypeface()).withIconTintingEnabled(true).withIcon(R.drawable.ic_contacts_white_24dp)
                                .withName("Contact Us").withIdentifier(5001).withSelectable(true).withTextColorRes(R.color.md_white_1000),*/
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withTypeface(FontHelper.getInstance(getApplicationContext()).getAppCustomTypeface()).withIconTintingEnabled(true).withIcon(R.drawable.ic_baseline_share_24)
                                .withName(R.string.txt_share_the_app).withIdentifier(402).withSelectable(true).withTextColorRes(R.color.md_white_1000),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withTypeface(FontHelper.getInstance(getApplicationContext()).getAppCustomTypeface()).withIconTintingEnabled(true).withIcon(R.drawable.ic_exit_to_app_white_24dp)
                                .withName(R.string.text_log_out).withIdentifier(401).withSelectable(true).withTextColorRes(R.color.md_white_1000)

                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.equals(401)) {
                            submitPopup();
                            return false;
                        } else if (drawerItem.equals(500)) {
                            getSupportFragmentManager().beginTransaction()
                                    .addToBackStack("privacyPolicyFragment")
                                    .replace(R.id.screenContainer, new PrivacyPolicyFragment())
                                    .commit();
                            return false;
                        } else if (drawerItem.equals(501)) {
                            getSupportFragmentManager().beginTransaction()
                                    .addToBackStack("refundPolicyFragment")
                                    .replace(R.id.screenContainer, new RefundPolicyFragment())
                                    .commit();
                            return false;
                        } else if (drawerItem.equals(502)) {
                            getSupportFragmentManager().beginTransaction()
                                    .addToBackStack("termsAndConditionFragment")
                                    .replace(R.id.screenContainer, new TermsAndConditionsFragment())
                                    .commit();
                            return false;
                        } else if (drawerItem.equals(20)) {
                            getSupportFragmentManager().beginTransaction()
                                    .addToBackStack("contactFragment")
                                    .replace(R.id.screenContainer, new SupportFragment())
                                    .commit();
                            return false;
                        } else if (drawerItem.equals(25)) {
                            getSupportFragmentManager().beginTransaction()
                                    .addToBackStack("aboutUsFragment")
                                    .replace(R.id.screenContainer, new AboutUsFragment())
                                    .commit();
                            return false;
                        } else if (drawerItem.equals(60)) {
                            /*Intent intent = new Intent(HomeActivity.this, YoutubeVideoActivity.class);
                            intent.putExtra("videoId", "-wH2uoxN_HU");
                            startActivity(intent);*/
                            return false;
                        } else if (drawerItem.equals(2000)) {
                            getSupportFragmentManager().beginTransaction()
                                    .addToBackStack("homeFragment")
                                    .replace(R.id.screenContainer, MockTestFragment.newInstance(null, "live"))
                                    .commit();
                            return false;
                        } else if (drawerItem.equals(2001)) {
                            getSupportFragmentManager().beginTransaction()
                                    .addToBackStack("homeFragment")
                                    .replace(R.id.screenContainer, MockTestFragment.newInstance("1", "live"))
                                    .commit();
                            return false;
                        } else if (drawerItem.equals(2003)) {
                            getSupportFragmentManager().beginTransaction()
                                    .addToBackStack("homeFragment")
                                    .replace(R.id.screenContainer, MockTestFragment.newInstance("100", "quiz"))
                                    .commit();
                            return false;
                        } else if (drawerItem.equals(402)) {
                            /*Create an ACTION_SEND Intent*/
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            /*This will be the actual content you wish you share.*/
                            String shareBody = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                            /*The type of the content is text, obviously.*/
                            intent.setType("text/plain");
                            /*Applying information Subject and Body.*/
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Download the App from Play Store: ");
                            intent.putExtra(Intent.EXTRA_TEXT, "Download the App from Play Store: \n\n\n"
                                    + shareBody);
                            /*Fire!*/
                            startActivity(Intent.createChooser(intent, "Share using..."));
                            return false;
                        } else {
                            return false;
                        }
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(false)
                .build();

        //only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 11
            result.setSelection(1, false);

            //set the active profile
            headerResult.setActiveProfile(profile);
        }

        result.updateBadge(4, new StringHolder(10 + ""));

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void submitPopup() {
        CommonView.INSTANCE.showRoundedAlertDialog(this, "Log Out",
                "Do you really want to log out ?", "Yes", "No",
                result -> {
                    if ((boolean) result) {
                        String token = tinyDB.getString(Constants.AUTH_TOKEN);
                        CandidateLogOut candidateLogOut = new CandidateLogOut(new ArrayList<Integer>(), token);
                        mLoginViewModel.callLogOut(candidateLogOut);
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }
}

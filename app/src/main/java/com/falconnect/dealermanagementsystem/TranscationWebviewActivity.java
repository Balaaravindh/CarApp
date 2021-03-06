package com.falconnect.dealermanagementsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.Adapter.CustomList;
import com.falconnect.dealermanagementsystem.Adapter.ManageFooterCustomAdapter;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.ManageFooterDataModel;
import com.falconnect.dealermanagementsystem.NavigationDrawer.BuyPageNavigation;
import com.falconnect.dealermanagementsystem.SharedPreference.PlandetailsSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;
import com.freshdesk.hotline.FaqOptions;
import com.freshdesk.hotline.Hotline;
import com.navdrawer.SimpleSideDrawer;

import java.util.ArrayList;
import java.util.HashMap;

public class TranscationWebviewActivity extends Activity {

    String title, url;
    WebView webView;
    ImageView my_queries_back;
    BuyPageNavigation queries_buypagenavigation;
    SessionManager session_queries;
    ImageView imageView_queries;
    TextView profile_name_queries;
    TextView profile_address_queries;
    HashMap<String, String> user;
    String saved_name_queries, saved_address_queries;
    ProgressDialog barProgressDialog;
    RelativeLayout result_found;
    PlandetailsSharedPreferences plandetailsSharedPreferences;
    HashMap<String, String> plan;
    private SimpleSideDrawer mNav_queries;
    ImageView trans_nav;
    private static RecyclerView Transcationrecycleview;
    private static ArrayList<ManageFooterDataModel> transcationdata;
    private static RecyclerView.Adapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transcation_webview);


        trans_nav = (ImageView) findViewById(R.id.main_dashboard_mnav);
        Transcationrecycleview = (RecyclerView) findViewById(R.id.transcation_recyclerview);
        Transcationrecycleview.setHasFixedSize(true);
        Transcationrecycleview.setLayoutManager(new LinearLayoutManager(TranscationWebviewActivity.this, LinearLayoutManager.HORIZONTAL, false));
        transcationdata = new ArrayList<ManageFooterDataModel>();
        for (int i = 0; i < MyFooterManageData.managenameArray.length; i++) {

            transcationdata.add(new ManageFooterDataModel(
                    MyFooterManageData.managenameArray[i],
                    MyFooterManageData.managedrawableArrayWhite5[i],
                    MyFooterManageData.manageid_[i]
            ));
        }
        adapter = new ManageFooterCustomAdapter(TranscationWebviewActivity.this, transcationdata);
        Transcationrecycleview.setAdapter(adapter);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        session_queries = new SessionManager(TranscationWebviewActivity.this);
        user = session_queries.getUserDetails();

        url = ConstantIP.IP + "mobileweb/transaction/index.html#/transaction/" + user.get("user_id");

        webView = (WebView) findViewById(R.id.transcation_webview);
        my_queries_back = (ImageView) findViewById(R.id.networks_back);
        barProgressDialog = ProgressDialog.show(TranscationWebviewActivity.this, "Loading...", "Please Wait ...", true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                barProgressDialog.dismiss();
            }

        });

        my_queries_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mNav_queries = new SimpleSideDrawer(this);
        mNav_queries.setLeftBehindContentView(R.layout.activity_behind_left_simple);

        imageView_queries = (ImageView) mNav_queries.findViewById(R.id.profile_avatar);
        profile_name_queries = (TextView) mNav_queries.findViewById(R.id.profile_name);
        profile_address_queries = (TextView) mNav_queries.findViewById(R.id.profile_address);

        session_queries = new SessionManager(TranscationWebviewActivity.this);
        user = session_queries.getUserDetails();
        saved_name_queries = user.get("dealer_name");
        saved_address_queries = user.get("dealershipname");
        profile_name_queries.setText(saved_name_queries);
        if (user.get("dealer_img").isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_avatar)
                    .transform(new RoundImageTransform(TranscationWebviewActivity.this))
                    .into(imageView_queries);
        } else {
            Glide.with(getApplicationContext())
                    .load(user.get("dealer_img"))
                    .transform(new RoundImageTransform(TranscationWebviewActivity.this))
                    .into(imageView_queries);
        }
        profile_address_queries.setText(saved_address_queries);

        my_queries_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNav_queries.toggleLeftDrawer();
            }
        });

        RelativeLayout call_layout = (RelativeLayout) mNav_queries.findViewById(R.id.call_layout);
        RelativeLayout chat_layout = (RelativeLayout) mNav_queries.findViewById(R.id.chat_layout);
        RelativeLayout logout_layout = (RelativeLayout) mNav_queries.findViewById(R.id.logout_layout);
        result_found = (RelativeLayout) findViewById(R.id.result_found);

        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertbox = new AlertDialog.Builder(TranscationWebviewActivity.this)
                        .setMessage("Do you want to Call?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:9790928569"));
                                startActivity(callIntent);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        })
                        .show();
            }
        });

        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session_queries.logoutUser();
                mNav_queries.closeLeftSide();
                TranscationWebviewActivity.this.finish();
            }
        });

        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat();
                mNav_queries.closeLeftSide();
            }
        });

        //NAVIGATION DRAWER LIST VIEW
        queries_buypagenavigation = new BuyPageNavigation();
        CustomList adapter = new CustomList(TranscationWebviewActivity.this, BuyPageNavigation.web, BuyPageNavigation.imageId);

        ListView list = (ListView) findViewById(R.id.nav_list_view);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (BuyPageNavigation.web[position] == "Dashboard") {
                    Intent intent = new Intent(TranscationWebviewActivity.this, MainDashBoardActivity.class);
                    startActivity(intent);
                    TranscationWebviewActivity.this.finish();
                    mNav_queries.closeLeftSide();
                    //Toast.makeText(MyQueriesActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Buy") {
                    Intent intent = new Intent(TranscationWebviewActivity.this, DashBoard.class);
                    startActivity(intent);
                    TranscationWebviewActivity.this.finish();
                    mNav_queries.closeLeftSide();
                    // Toast.makeText(MyQueriesActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Sell") {
                    Intent intent = new Intent(TranscationWebviewActivity.this, SellDashBoardActivity.class);
                    startActivity(intent);
                    TranscationWebviewActivity.this.finish();
                    mNav_queries.closeLeftSide();
                    //  Toast.makeText(MyQueriesActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Manage") {
                    Intent intent = new Intent(TranscationWebviewActivity.this, ManageDashBoardActivity.class);
                    startActivity(intent);
                    TranscationWebviewActivity.this.finish();
                    mNav_queries.closeLeftSide();
                    //Toast.makeText(NetworksWebviewActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Funding") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(TranscationWebviewActivity.this);
                    plan = plandetailsSharedPreferences.getUserDetails();

//                    if ( plan.get("PlanName").equals("BASIC")) {
//
//                        Intent intent = new Intent(TranscationWebviewActivity.this, LoanActivity.class);
//                        startActivity(intent);
//                        mNav_queries.closeLeftSide();
//                        TranscationWebviewActivity.this.finish();
//                    } else {
                        Intent intent = new Intent(TranscationWebviewActivity.this, FundingActivity.class);
                        startActivity(intent);
                        mNav_queries.closeLeftSide();
                        TranscationWebviewActivity.this.finish();
//                    }
                    //   Toast.makeText(MyQueriesActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Networks") {
                    String url = "http://app.dealerplus.in/user_login_cometchat?session_user_id=" + user.get("user_id");
                    Intent intet = new Intent(TranscationWebviewActivity.this, NetworksWebviewActivity.class);
                    //intet.putExtra("title", BuyPageNavigation.web[position]);
                    intet.putExtra("url", url);
                    startActivity(intet);
                    mNav_queries.closeLeftSide();
                    // Toast.makeText(MyQueriesActivity.this, BuyPageNavigation.web[position], Toast.LENGTH_SHORT).show();
                } else if (BuyPageNavigation.web[position] == "Reports") {
                    plandetailsSharedPreferences = new PlandetailsSharedPreferences(TranscationWebviewActivity.this);
                    plan = plandetailsSharedPreferences.getUserDetails();
                    if (plan.get("PlanName").equals("BASIC")) {
                        Intent intent = new Intent(TranscationWebviewActivity.this, BuyPlanActivity.class);
                        startActivity(intent);
                        //MainDashBoardActivity.this.finish();
                        mNav_queries.closeLeftSide();
                    } else {
                        Intent intent = new Intent(TranscationWebviewActivity.this, ReportSalesActivity.class);
                        startActivity(intent);
                        TranscationWebviewActivity.this.finish();
                        mNav_queries.closeLeftSide();
                    }
                } else if (BuyPageNavigation.web[position] == "FAQs") {
                    faqs();
                    mNav_queries.closeLeftSide();
                } else {
                    //Toast.makeText(DashBoard.this, web[position], Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void faqs() {
        FaqOptions faqOptions = new FaqOptions()
                .showFaqCategoriesAsGrid(true)
                .showContactUsOnAppBar(true)
                .showContactUsOnFaqScreens(false)
                .showContactUsOnFaqNotHelpful(false);

        Hotline.showFAQs(TranscationWebviewActivity.this, faqOptions);
    }

    public void chat() {
        Hotline.showConversations(getApplicationContext());
        // Hotline.clearUserData(getApplicationContext());
    }

    @Override
    public void onBackPressed() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        TranscationWebviewActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }



}

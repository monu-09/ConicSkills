package com.conicskill.app.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.conicskill.app.BuildConfig;
import com.conicskill.app.R;
import com.conicskill.app.base.BaseActivity;
import com.conicskill.app.data.model.payments.ItemListItem;
import com.conicskill.app.data.model.payments.PaymentData;
import com.conicskill.app.data.model.payments.PaymentRequest;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.util.Constants;
import com.conicskill.app.util.OnResponse;
import com.conicskill.app.util.TinyDB;
import com.conicskill.app.util.Utils;
import com.paykun.sdk.PaykunApiCall;
import com.paykun.sdk.PaykunTransaction;
import com.paykun.sdk.PaymentMessage;
import com.paykun.sdk.PaymentMethods;
import com.paykun.sdk.PaymentTypes;
import com.paykun.sdk.SubPaymentTypes;
import com.paykun.sdk.Sub_Methods;
import com.paykun.sdk.helper.PaykunHelper;
import com.paykun.sdk.helper.PaykunResponseListener;
import com.paykun.sdk.logonsquare.Transaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;

public class PaymentActivity extends BaseActivity implements PaykunResponseListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.paymentAnimationView)
    LottieAnimationView paymentAnimationView;

    @BindView(R.id.textViewPaymentStatus)
    AppCompatTextView textViewPaymentStatus;

    @BindView(R.id.textViewDisclaimer)
    AppCompatTextView textViewDisclaimer;

    @Inject
    ViewModelFactory viewModelFactory;
    TinyDB tinyDB;
    ArrayList<ItemListItem> arrayList = new ArrayList<>();
    private PaymentViewModel paymentViewModel;

    @Override
    protected int layoutRes() {
        return R.layout.activity_payment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paymentViewModel = new ViewModelProvider(this, viewModelFactory).get(PaymentViewModel.class);
        observeViewModel();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = ContextCompat.getDrawable(getApplicationContext(), R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.md_white_1000), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        tinyDB = new TinyDB(getApplicationContext());
        getIntent().getStringExtra("price");
        arrayList = (ArrayList<ItemListItem>) getIntent().getSerializableExtra("payments");
        createPayment();
    }

    public void observeViewModel() {
        paymentViewModel.getError().observe(this, error -> {
            if (error) {

            }
        });

        paymentViewModel.getLoading().observe(this, isLoading -> {
            if (isLoading) {

            } else {

            }
        });

        paymentViewModel.observePaymentResponse().observe(this, paymentResponseResponse -> {
            if (paymentResponseResponse.body() != null) {
                if (paymentResponseResponse.code() == HttpsURLConnection.HTTP_OK) {
                    Intent intent = new Intent(PaymentActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        textViewDisclaimer.setOnClickListener(l->{
            if(textViewDisclaimer.getText().toString().equalsIgnoreCase(getString(R.string.txt_click_to_go_back))) {
                finish();
            }
        });
    }

    /*@SuppressLint("SetTextI18n")
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getResults(Events.PaymentMessage message) {
        if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_SUCCESS)) {
            if (!TextUtils.isEmpty(message.getTransactionId())) {
                paymentAnimationView.setAnimation(R.raw.payment_success);
                paymentAnimationView.playAnimation();
                textViewPaymentStatus.setText(R.string.txt_payment_success);
                textViewPaymentStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.md_green_800));
                // call payment animation view
                PaymentData paymentData = new PaymentData();
                paymentData.setCandidateId(tinyDB.getString(Constants.CANDIDATE_ID));
                paymentData.setCompanyId(BuildConfig.COMPANY_ID);
                paymentData.setPaymentId(message.getTransactionId());

                paymentData.setItemList(arrayList);
                PaymentRequest paymentRequest = new PaymentRequest();
                paymentRequest.setData(paymentData);
                paymentViewModel.callProcessPayment(paymentRequest);
            }
        } else if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_FAILED)) {
            // do your stuff here
            Toast.makeText(PaymentActivity.this, "Your Transaction is failed", Toast.LENGTH_SHORT).show();
            finish();
        } else if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_SERVER_ISSUE)) {
            // do your stuff here
            Toast.makeText(PaymentActivity.this, PaykunHelper.MESSAGE_SERVER_ISSUE, Toast.LENGTH_SHORT).show();
            finish();
        } else if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_ACCESS_TOKEN_MISSING)) {
            // do your stuff here
            Toast.makeText(PaymentActivity.this, "Access Token missing", Toast.LENGTH_SHORT).show();
            finish();
        } else if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_MERCHANT_ID_MISSING)) {
            // do your stuff here
            Toast.makeText(PaymentActivity.this, "Merchant Id is missing", Toast.LENGTH_SHORT).show();
            finish();
        } else if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_INVALID_REQUEST)) {
            Toast.makeText(PaymentActivity.this, "Invalid Request", Toast.LENGTH_SHORT).show();
            finish();
        } else if (message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_NETWORK_NOT_AVAILABLE)) {
            Toast.makeText(PaymentActivity.this, "Network is not available", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Register this activity to listen to event.
        GlobalBus.getBus().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unregister from activity
        GlobalBus.getBus().unregister(this);
    }*/

    /**
     * Called when the activity has detected the user's press of the back
     * key. The {@link #getOnBackPressedDispatcher() OnBackPressedDispatcher} will be given a
     * chance to handle the back button before the default behavior of
     * {@link Activity#onBackPressed()} is invoked.
     *
     * @see #getOnBackPressedDispatcher()
     */
    @Override
    public void onBackPressed() {
        Utils.showRoundedAlertDialog(PaymentActivity.this, getString(R.string.text_do_want_go_back),
                getString(R.string.txt_current_transaction), true,
                true, "Yes", "No", new OnResponse() {
                    @Override
                    public void onResponse(Object result) {
                        if ((boolean) result) {
                            Utils.alertDialog.dismiss();
                            finish();
                        } else {
                            Utils.alertDialog.dismiss();
                        }
                    }
                });
    }

    public  void createPayment()
    {
        String merchantIdLive= BuildConfig.PAYKUN_MERCHANT_ID ;
        String accessTokenLive= BuildConfig.PAYKUN_ACCESS_TOKEN;
        String productName=getIntent().getStringExtra("name");
        String orderId=String.valueOf(System.currentTimeMillis());
        String amount=getIntent().getStringExtra("price");
        String customerName=tinyDB.getString(Constants.NAME);
        String customerPhone=tinyDB.getString(Constants.PHONE_NUMBER);
        String customerEmail=tinyDB.getString(Constants.EMAIL);

        HashMap<PaymentTypes, PaymentMethods> payment_methods = new HashMap<>();
        PaymentMethods paymentMethods=new PaymentMethods();
        paymentMethods.enable=true;
        paymentMethods.priority=1;
        paymentMethods.set_as_prefered=true;
        payment_methods.put(PaymentTypes.UPI,paymentMethods);
        paymentMethods=new PaymentMethods();
        paymentMethods.enable=true;
        paymentMethods.priority=2;
        paymentMethods.set_as_prefered=true;
        payment_methods.put(PaymentTypes.NB,paymentMethods);
        paymentMethods=new PaymentMethods();
        paymentMethods.enable=false;
        paymentMethods.priority=3;
        paymentMethods.set_as_prefered=false;
        payment_methods.put(PaymentTypes.WA,paymentMethods);
        paymentMethods=new PaymentMethods();
        paymentMethods.enable=true;
        paymentMethods.priority=3;
        paymentMethods.set_as_prefered=true;
        payment_methods.put(PaymentTypes.DCCC,paymentMethods);
        paymentMethods=new PaymentMethods();
        paymentMethods.enable=true;
        paymentMethods.priority=3;
        paymentMethods.set_as_prefered=true;
        payment_methods.put(PaymentTypes.UPIQRCODE,paymentMethods);
        paymentMethods=new PaymentMethods();
        paymentMethods.enable=true;
        paymentMethods.priority=3;
        paymentMethods.set_as_prefered=true;
        payment_methods.put(PaymentTypes.EMI,paymentMethods);
        //--------------------------------------
        PaykunTransaction paykunTransaction=new PaykunTransaction(merchantIdLive,accessTokenLive,true);
        try {
            paykunTransaction.setCurrency("INR");
            paykunTransaction.setCustomer_name(customerName);
            paykunTransaction.setCustomer_email(customerEmail);
            paykunTransaction.setCustomer_phone(customerPhone);
            paykunTransaction.setProduct_name(productName);
            paykunTransaction.setOrder_no(String.valueOf(System.currentTimeMillis()));
            paykunTransaction.setTheme_color("<COLOR CODE>");
            paykunTransaction.setPayment_methods(payment_methods);
            paykunTransaction.setAmount(amount);
            paykunTransaction.setOrder_no(orderId);
            paykunTransaction.setLive(true);
            new PaykunApiCall.Builder(PaymentActivity.this).sendJsonObject(paykunTransaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(Transaction paymentMessage) {
        if (!TextUtils.isEmpty(paymentMessage.getPaymentId())) {
            paymentAnimationView.setAnimation(R.raw.payment_success);
            paymentAnimationView.playAnimation();
            textViewPaymentStatus.setText(R.string.txt_payment_success);
            textViewPaymentStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.md_green_800));
            // call payment animation view
            PaymentData paymentData = new PaymentData();
            paymentData.setCandidateId(tinyDB.getString(Constants.CANDIDATE_ID));
            paymentData.setCompanyId(BuildConfig.COMPANY_ID);
            paymentData.setPaymentId(paymentMessage.getPaymentId());

            paymentData.setItemList(arrayList);
            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setData(paymentData);
            paymentViewModel.callProcessPayment(paymentRequest);
        }
    }

    @Override
    public void onPaymentError(PaymentMessage message) {
        if(message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_CANCELLED)){
            // do your stuff here
            Toast.makeText(this,"Your Transaction is cancelled",Toast.LENGTH_SHORT).show();
        }
        else if(message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_FAILED)){
            // do your stuff here
            Toast.makeText(this,"Your Transaction is failed",Toast.LENGTH_SHORT).show();
        }
        else if(message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_NETWORK_NOT_AVAILABLE)){
            // do your stuff here
            Toast.makeText(this,"Internet Issue",Toast.LENGTH_SHORT).show();

        }else if(message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_SERVER_ISSUE)){
            // do your stuff here
            Toast.makeText(this,"Server issue",Toast.LENGTH_SHORT).show();
        }else if(message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_ACCESS_TOKEN_MISSING)){
            // do your stuff here
            Toast.makeText(this,"Access Token missing",Toast.LENGTH_SHORT).show();
        }
        else if(message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_MERCHANT_ID_MISSING)){
            // do your stuff here
            Toast.makeText(this,"Merchant Id is missing",Toast.LENGTH_SHORT).show();
        }
        else if(message.getResults().equalsIgnoreCase(PaykunHelper.MESSAGE_INVALID_REQUEST)){
            Toast.makeText(this,"Invalid Request",Toast.LENGTH_SHORT).show();
        }
    }
}

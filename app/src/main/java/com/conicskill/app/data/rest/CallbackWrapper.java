package com.conicskill.app.data.rest;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * @author vikas@cprep.in
 * @date 12-March-2019
 * @use This call is a wrapper used for making an API call and then use it as
 * @param <T>
 */
public abstract class CallbackWrapper<T extends Response> extends DisposableObserver<T> {
    //BaseView is just a reference of a View in MVP
    private WeakReference<CallbackListener> weakReference;

    public CallbackWrapper(CallbackListener callbackListener) {
        this.weakReference = new WeakReference<>(callbackListener);
    }

    protected abstract void onSuccess(T t);

    @Override
    public void onNext(T t) {
        //You can return StatusCodes of different cases from your API and handle it here. I usually
        // include these cases on BaseResponse and inherit it from every Response
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) e).response().errorBody();
        } else if (e instanceof SocketTimeoutException) {

        } else if (e instanceof IOException) {

        } else {
        }
    }

    @Override
    public void onComplete() {

    }

    private String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("message");
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
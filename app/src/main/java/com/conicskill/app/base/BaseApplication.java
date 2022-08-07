package com.conicskill.app.base;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.conicskill.app.R;
import com.conicskill.app.di.component.ApplicationComponent;
import com.conicskill.app.di.component.DaggerApplicationComponent;
import com.conicskill.app.util.FontHelper;
import com.downloader.PRDownloader;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.yausername.ffmpeg.FFmpeg;
import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.YoutubeDLException;

import androidx.appcompat.app.AppCompatDelegate;
import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.GlobalScope;

/**
 * @author vikas@cprep.in
 * @use The application class is used to extend the DaggerApplication in order to inject the dependencies
 * that will be used thorugh out the application context such as Fonts and ImageLoader for the
 * Material Drawer and other activities.
 */
public class BaseApplication extends DaggerApplication {

    public static int NOTIFICATION_ID = 100;

    public static Fetch fetch;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FontHelper.getInstance(getApplicationContext()).overrideFont();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

		PRDownloader.initialize(getApplicationContext());

        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(this)
                .setDownloadConcurrentLimit(3)
                .build();

        fetch = Fetch.Impl.getInstance(fetchConfiguration);

        Completable.fromAction(this::initLibraries).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                // it worked
//                Toast.makeText(getApplicationContext(), "initialization success: ", Toast.LENGTH_SHORT).show();
//
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getApplicationContext(), "initialization failed: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        ApplicationComponent component = DaggerApplicationComponent.builder().application(this).build();
        component.inject(this);

        return component;
    }

    private void initLibraries() throws YoutubeDLException {
        YoutubeDL.getInstance().init(this);
        FFmpeg.getInstance().init(this);
    }
}


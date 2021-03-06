package com.nanocomputing.testad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.ads.mobitechadslib.AdsModel;
import com.ads.mobitechadslib.MobiAdBanner;
import com.ads.mobitechadslib.MobitechAds;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.Response;

import static com.ads.mobitechadslib.MobitechAds.getBannerAd;
import static com.ads.mobitechadslib.MobitechAds.getBannerAdValues;

public class MainActivity extends AppCompatActivity {
    private AdsModel adsModel ;
    private MobiAdBanner mobiAdBanner;
    private CompositeDisposable disposable = new CompositeDisposable();
    private String adCategory="3"; //specify the ad category you want to show.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // ....................Intertistial Ad ...............
        MobitechAds.getIntertistialAd(
                MainActivity.this,
                adCategory);
        // ...................End of Intertistial ad............



        // ----------------------Banner Ad --------------------.
        mobiAdBanner = findViewById(R.id.bannerAd);
        mobiAdBanner.setOnClickListener(v -> {
            mobiAdBanner.viewBannerAd(MainActivity.this,
                    adsModel.getAd_urlandroid());
        });
        Timer timer = new Timer();
        TimerTask myTask = new TimerTask() {
            @Override
            public void run() {
                Observable<Response> observable = getBannerAd(adCategory);
                observable.subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }
                    @Override
                    public void onNext(Response response) {
                        try {
                            adsModel = getBannerAdValues(response.body().string());
                            mobiAdBanner.showAd(getApplicationContext(),
                                    adsModel.getAd_upload());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("Banner", "onError : "+e.getMessage() );
                    }
                    @Override
                    public void onComplete() {
                    }
                });
            }
        };timer.schedule(myTask, 100, 20000);//refresh after

//...............................end of banner ad ........................


        Button showBanner = findViewById(R.id.showbanner);
        showBanner.setOnClickListener(v->{
            MobitechAds.getIntertistialAd(
                    MainActivity.this,
                    adCategory);
        });


    }
}

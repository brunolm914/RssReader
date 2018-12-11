package com.npg.rssreader.RssCartelera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.npg.rssreader.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RssActivity extends AppCompatActivity {
    private TextView rss_channel_title, rss_film_title, rss_film_gender, rss_film_duration, rss_film_rating;
    private ImageView button;

    public static Context rssContext;
    public static Activity rssActivity;
    public static int posicion = 0;
    private View rssView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.rss_films_layout);
        rssContext = getApplicationContext();
        rssActivity = this;
        EnCarteleraRSS enCarteleraRSS=new EnCarteleraRSS();

    }



    //    public static void setCardViewItems() {
////        Creamos el runnable y lo ejecutamos inmediantamente
//        mosaicBannerHandler = new Handler(Looper.getMainLooper());
//        mosaicBannerHandler.removeCallbacks(start_rss_runable);
//        mosaicBannerHandler.postDelayed(start_rss_runable, 0);
//    }
//
//    static Runnable start_rss_runable = new Runnable() {
//        public void run() {
//
//            final boolean[] cargado = {false};
//
//            final TextView rss_channel_title;
//            final TextView rss_film_title;
//            final TextView rss_film_gender;
//            final TextView rss_film_duration;
//            ImageView rss_film_image;
////            Bindeo y setteo de la imagen
//            rss_film_image = RssActivity.rssActivity.findViewById(R.id.rss_film_image);
//            Picasso.get().load(mFeedModelList.get(posicion).imageLink).into(rss_film_image);
//            rss_film_image.setBackground(rss_film_image.getDrawable());
////            Bindeo y setteo del titulo del RSS
//            rss_channel_title = RssActivity.rssActivity.findViewById(R.id.textView);
//            rss_channel_title.setText(mFeedModelList.get(posicion).title);
////            Bindeo y setteo del titulo de la pelicula
//            rss_film_title = RssActivity.rssActivity.findViewById(R.id.rss_film_title);
//            rss_film_title.setText(mFeedModelList.get(posicion).title);
////            Bindeo y setteo del genero de la pelicula
//            rss_film_gender = RssActivity.rssActivity.findViewById(R.id.rss_film_gender);
//            rss_film_gender.setText(getGenderFromDescription(mFeedModelList.get(posicion).description));
////            Bindeo y setteo de la duracion de la pelicula
//            rss_film_duration = RssActivity.rssActivity.findViewById(R.id.rss_film_duration);
//            rss_film_duration.setText(getDurationFromDescription(mFeedModelList.get(posicion).description));
////          Animaciones
//            Animation a = AnimationUtils.loadAnimation(RssActivity.rssContext, R.anim.rss_text_animation);
//            rss_film_image.startAnimation(a);
//            rss_channel_title.startAnimation(a);
//            rss_film_title.startAnimation(a);
//            rss_film_gender.startAnimation(a);
//            rss_film_duration.startAnimation(a);
//
////          Aumentamos posicion para recorrer la lista de rss
//            if (RssActivity.posicion >= mFeedModelList.size() - 1) {
//                RssActivity.posicion = 0;
//            } else
//                RssActivity.posicion++;
//
////            Pasados x segundos se relanza el runnable
//            mosaicBannerHandler.postDelayed(this, 5000);
//
//
//        }
//    };
//    public static String getGenderFromDescription(String description) {
//        String gender = "";
//        if (description.length() > 2) {
//            for (int i = 0; i < description.length() - 1; i++) {
//                if (description.charAt(i) == ' ' && description.charAt(i+1) == ' ') {
//                    gender = description.substring(0, i);
//                    gender = gender.replace("<p>", "");
//                    break;
//                }
//            }
//        }
//        if(gender.equals(""))
//            gender= RssActivity.rssActivity.getResources().getString(R.string.rss_noinfo);
//        return gender;
//    }
//
//    public static String getDurationFromDescription(String description) {
//        String duration = "";
//        if (description.length() > 2) {
//            for (int i = 0; i < description.length() - 1; i++) {
//                if (description.charAt(i) == ' ' && description.charAt(i+1) == '-') {
//                    duration = description.substring(0, i);
//                    duration = duration.replace("<p>", "");
//                    duration = duration.replace(getGenderFromDescription(description) + " ", "");
//                    break;
//                }
//            }
//        }
//        if(duration.equals(""))
//            duration= RssActivity.rssActivity.getResources().getString(R.string.rss_noinfo);
//        return duration;
//    }
//
//    public static float getMediaScore(String description) {
//        float mediascore = -1;
//        int indexIni;
//        String mediascorestring;
//        if (description.length() > 2) {
//            indexIni = description.indexOf("alt=");
//            mediascorestring = description.substring(indexIni + 5, indexIni + 8);
//            if (mediascorestring.contains(".")) {
//                mediascore = Float.parseFloat(mediascorestring);
//            } else {
//                mediascore = Float.parseFloat(mediascorestring.substring(0, 1));
//            }
//        }
//        return mediascore;
//    }
//
//    public static float getUserScore(String description) {
//        float userscore = -1;
//        int indexIni;
//        String userscorestring;
//        if (description.length() > 2) {
//            while (description.contains("alt=")) {
//                description = description.replaceFirst("alt=", "");
//                if (description.contains("alt=")) {
//                    indexIni = description.indexOf("alt=");
//                    userscorestring = description.substring(indexIni + 5, indexIni + 8);
//                    if (userscorestring.contains(".")) {
//                        userscore = Float.parseFloat(userscorestring);
//                    }
//                    else {
//                        userscore = Float.parseFloat(userscorestring.substring(0, 1)); }
//                }
//            }
//        }
//        return userscore;
//    }

}

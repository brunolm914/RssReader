package com.npg.rssreader.RssCartelera;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.npg.rssreader.R;
import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
public class EnCarteleraRSS {
    public EnCarteleraRSS() {
        new EnCarteleraFeedTask().execute((Void) null);
    }

    public static List<RssFeedModel> mFeedModelList;

    public static Handler mosaicBannerHandler;
//    Inicia el handler que se encarga de settear los elementos
    public static void setCardViewItems() {
//        Creamos el runnable y lo ejecutamos inmediantamente
        mosaicBannerHandler = new Handler(Looper.getMainLooper());
        mosaicBannerHandler.removeCallbacks(start_rss_runable);
        mosaicBannerHandler.postDelayed(start_rss_runable, 0);
    }
//  Runnable que gestiona el RSS
    static Runnable start_rss_runable = new Runnable() {
        public void run() {

            final TextView rss_channel_title;
            final TextView rss_film_title;
            final TextView rss_film_gender;
            final TextView rss_film_duration;
            final ImageView rss_film_image;
//            Bindeo y setteo de la imagen
            rss_film_image = RssActivity.rssActivity.findViewById(R.id.rss_film_image);
            Picasso.get().load(mFeedModelList.get(RssActivity.posicion).imageLink).into(rss_film_image);
            rss_film_image.setBackground(rss_film_image.getDrawable());
//            Bindeo y setteo del titulo del RSS
            rss_channel_title = RssActivity.rssActivity.findViewById(R.id.textView);
            rss_channel_title.setText(RssActivity.rssActivity.getResources().getString(R.string.rss_title_PeliculasEnCartelera));
//            Bindeo y setteo del titulo de la pelicula
            rss_film_title = RssActivity.rssActivity.findViewById(R.id.rss_film_title);
            rss_film_title.setText(mFeedModelList.get(RssActivity.posicion).title);
//            Bindeo y setteo del genero de la pelicula
            rss_film_gender = RssActivity.rssActivity.findViewById(R.id.rss_film_gender);
            rss_film_gender.setText(getGenderFromDescription(mFeedModelList.get(RssActivity.posicion).description));
//            Bindeo y setteo de la duracion de la pelicula
            rss_film_duration = RssActivity.rssActivity.findViewById(R.id.rss_film_duration);
            rss_film_duration.setText(getDurationFromDescription(mFeedModelList.get(RssActivity.posicion).description));

//          Animaciones
            Animation a = AnimationUtils.loadAnimation(RssActivity.rssContext, R.anim.rss_text_animation);
            rss_film_image.startAnimation(a);
            rss_channel_title.startAnimation(a);
            rss_film_title.startAnimation(a);
            rss_film_gender.startAnimation(a);
            rss_film_duration.startAnimation(a);

//          Aumentamos posicion para recorrer la lista de rss
            if (RssActivity.posicion >= mFeedModelList.size() - 1) {
                RssActivity.posicion = 0;
            } else
                RssActivity.posicion++;

//            Pasados x segundos se relanza el runnable
            mosaicBannerHandler.postDelayed(this, 5000);


        }
    };
    public static String getGenderFromDescription(String description) {
        String gender = "";
        if (description.length() > 2) {
            for (int i = 0; i < description.length() - 1; i++) {
                if (description.charAt(i) == ' ' && description.charAt(i+1) == ' ') {
                    gender = description.substring(0, i);
                    gender = gender.replace("<p>", "");
                    break;
                }
            }
        }
        if(gender.equals(""))
            gender= RssActivity.rssActivity.getResources().getString(R.string.rss_noinfo);
        return gender;
    }

    public static String getDurationFromDescription(String description) {
        String duration = "";
        if (description.length() > 2) {
            for (int i = 0; i < description.length() - 1; i++) {
                if (description.charAt(i) == ' ' && description.charAt(i+1) == '-') {
                    duration = description.substring(0, i);
                    duration = duration.replace("<p>", "");
                    duration = duration.replace(getGenderFromDescription(description) + " ", "");
                    break;
                }
            }
        }
        if(duration.equals(""))
            duration= RssActivity.rssActivity.getResources().getString(R.string.rss_noinfo);
        return duration;
    }

    public static float getMediaScore(String description) {
        float mediascore = -1;
        int indexIni;
        String mediascorestring;
        if (description.length() > 2) {
            indexIni = description.indexOf("alt=");
            mediascorestring = description.substring(indexIni + 5, indexIni + 8);
            if (mediascorestring.contains(".")) {
                mediascore = Float.parseFloat(mediascorestring);
            } else {
                mediascore = Float.parseFloat(mediascorestring.substring(0, 1));
            }
        }
        return mediascore;
    }

    public static float getUserScore(String description) {
        float userscore = -1;
        int indexIni;
        String userscorestring;
        if (description.length() > 2) {
            while (description.contains("alt=")) {
                description = description.replaceFirst("alt=", "");
                if (description.contains("alt=")) {
                    indexIni = description.indexOf("alt=");
                    userscorestring = description.substring(indexIni + 5, indexIni + 8);
                    if (userscorestring.contains(".")) {
                        userscore = Float.parseFloat(userscorestring);
                    }
                    else {
                        userscore = Float.parseFloat(userscorestring.substring(0, 1)); }
                }
            }
        }
        return userscore;
    }
    public class EnCarteleraFeedTask extends AsyncTask<Void, Void, Boolean> {

        private String urlLink;
        private int posicion = 0;

        @Override
        protected void onPreExecute() {
            urlLink = "http://rss.sensacine.com/cine/encartelera?format=xml";
        }

        @Override
        protected Boolean doInBackground(Void... voids) {


            if (TextUtils.isEmpty(urlLink))
                return false;

            try {
                if (!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                    urlLink = "http://" + urlLink;

                URL url = new URL(urlLink);
                InputStream inputStream = url.openConnection().getInputStream();
                EnCarteleraRSS.mFeedModelList = parseFeedEncartelera(inputStream);
                return true;
            } catch (IOException e) {
                Log.e("EnCarteleraFeedTask", "Error", e);
            } catch (XmlPullParserException e) {
                Log.e("EnCarteleraFeedTask", "Error", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {


            if (success) {

                // Settear primera posicion
                posicion = RssActivity.posicion;
                EnCarteleraRSS.setCardViewItems();


            } else {
                Toast.makeText(RssActivity.rssContext,
                        "Enter a valid Rss feed url",
                        Toast.LENGTH_LONG).show();
            }
        }

        public List<RssFeedModel> parseFeedEncartelera(InputStream inputStream) throws XmlPullParserException,
                IOException {
            String title = null;
            String link = null;
            String description = null;
            String imageLink = null;
            boolean isItem = false;
            boolean isChannel = false;
            boolean isTitle = false;
            List<RssFeedModel> items = new ArrayList<>();
            if (!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                urlLink = "http://" + urlLink;

            try {
                XmlPullParser xmlPullParser = Xml.newPullParser();
                xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xmlPullParser.setInput(inputStream, null);

                xmlPullParser.nextTag();
                while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                    int eventType = xmlPullParser.getEventType();

                    String name = xmlPullParser.getName();

                    if (name == null)
                        continue;

                    if (eventType == XmlPullParser.END_TAG) {
                        if (name.equalsIgnoreCase("item")) {
                            isItem = false;
                        }
                        continue;
                    }

                    if (eventType == XmlPullParser.START_TAG) {
                        if (name.equalsIgnoreCase("item")) {
                            isItem = true;
                            continue;
                        }

                    }
                    Log.d("MyXmlParser", "Parsing name ==> " + name);
                    String result = "";

                    if (xmlPullParser.next() == XmlPullParser.TEXT) {
                        result = xmlPullParser.getText();
                    }

                    if (name.equalsIgnoreCase("title")) {
                        title = result;
                    } else if (name.equalsIgnoreCase("link")) {
                        link = result;
                    } else if (name.equalsIgnoreCase("description")) {
                        description = result;
                    } else if (name.equalsIgnoreCase("enclosure")) {
                        imageLink = xmlPullParser.getAttributeValue(0);


                    }


                    if (title != null && link != null && description != null && imageLink != null) {
                        if (isItem) {

                            RssFeedModel item = new RssFeedModel(title, link, description, imageLink);
                            items.add(item);
                        } else {

                        }

                        title = null;
                        link = null;
                        description = null;
                        imageLink = null;
                        isItem = false;

                    }
                }

                return items;
            } finally {
                inputStream.close();
            }
        }
    }
}

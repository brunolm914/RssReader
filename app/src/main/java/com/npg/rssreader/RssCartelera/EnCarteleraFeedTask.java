//package com.npg.rssreader.RssCartelera;
//
//import android.os.AsyncTask;
//import android.text.TextUtils;
//import android.util.Log;
//import android.util.Xml;
//import android.widget.Toast;
//
//import org.xmlpull.v1.XmlPullParser;
//import org.xmlpull.v1.XmlPullParserException;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//
//
//public class EnCarteleraFeedTask extends AsyncTask<Void, Void, Boolean> {
//
//    private String urlLink;
//    private int posicion = 0;
//
//    @Override
//    protected void onPreExecute() {
//        urlLink = "http://rss.sensacine.com/cine/encartelera?format=xml";
//    }
//
//    @Override
//    protected Boolean doInBackground(Void... voids) {
//
//
//        if (TextUtils.isEmpty(urlLink))
//            return false;
//
//        try {
//            if (!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
//                urlLink = "http://" + urlLink;
//
//            URL url = new URL(urlLink);
//            InputStream inputStream = url.openConnection().getInputStream();
//            EnCarteleraRSS.mFeedModelList = parseFeedEncartelera(inputStream);
//            return true;
//        } catch (IOException e) {
//            Log.e("EnCarteleraFeedTask", "Error", e);
//        } catch (XmlPullParserException e) {
//            Log.e("EnCarteleraFeedTask", "Error", e);
//        }
//        return false;
//    }
//
//    @Override
//    protected void onPostExecute(Boolean success) {
//
//
//        if (success) {
//
//            // Settear primera posicion
//            posicion = RssActivity.posicion;
//            EnCarteleraRSS.setCardViewItems();
//
//
//        } else {
//            Toast.makeText(RssActivity.rssContext,
//                    "Enter a valid Rss feed url",
//                    Toast.LENGTH_LONG).show();
//        }
//    }
//    public List<RssFeedModel> parseFeedEncartelera(InputStream inputStream) throws XmlPullParserException,
//            IOException {
//        String title = null;
//        String link = null;
//        String description = null;
//        String imageLink = null;
//        boolean isItem = false;
//        boolean isChannel = false;
//        boolean isTitle = false;
//        List<RssFeedModel> items = new ArrayList<>();
//        if (!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
//            urlLink = "http://" + urlLink;
//
//        try {
//            XmlPullParser xmlPullParser = Xml.newPullParser();
//            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
//            xmlPullParser.setInput(inputStream, null);
//
//            xmlPullParser.nextTag();
//            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
//                int eventType = xmlPullParser.getEventType();
//
//                String name = xmlPullParser.getName();
//
//                if (name == null)
//                    continue;
//
//                if (eventType == XmlPullParser.END_TAG) {
//                    if (name.equalsIgnoreCase("item")) {
//                        isItem = false;
//                    }
//                    continue;
//                }
//
//                if (eventType == XmlPullParser.START_TAG) {
//                    if (name.equalsIgnoreCase("item")) {
//                        isItem = true;
//                        continue;
//                    }
//
//                }
//                Log.d("MyXmlParser", "Parsing name ==> " + name);
//                String result = "";
//
//                if (xmlPullParser.next() == XmlPullParser.TEXT) {
//                    result = xmlPullParser.getText();
//                }
//
//                if (name.equalsIgnoreCase("title")) {
//                    title = result;
//                } else if (name.equalsIgnoreCase("link")) {
//                    link = result;
//                } else if (name.equalsIgnoreCase("description")) {
//                    description = result;
//                } else if (name.equalsIgnoreCase("enclosure")) {
//                    imageLink = xmlPullParser.getAttributeValue(0);
//
//
//                }
//
//
//                if (title != null && link != null && description != null && imageLink != null) {
//                    if (isItem) {
//
//                        RssFeedModel item = new RssFeedModel(title, link, description, imageLink);
//                        items.add(item);
//                    } else {
//
//                    }
//
//                    title = null;
//                    link = null;
//                    description = null;
//                    imageLink = null;
//                    isItem = false;
//
//                }
//            }
//
//            return items;
//        } finally {
//            inputStream.close();
//        }
//    }
//
//
//
//
//
//
//}
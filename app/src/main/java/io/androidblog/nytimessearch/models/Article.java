package io.androidblog.nytimessearch.models;


import android.graphics.Movie;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Article implements Parcelable {

    private String webUrl;
    private String headline;
    private String thumbNail;
    private int thumbNailHeight;
    private int thumbNailWidth;

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbNail() {
        return thumbNail;
    }
    public int getThumbNailHeight() {
        return thumbNailHeight;
    }

    public int getThumbNailWidth() {
        return thumbNailWidth;
    }

    public Article(JSONObject jsonObject) throws JSONException {
        this.webUrl = jsonObject.getString("web_url");
        this.headline = jsonObject.getJSONObject("headline").getString("main");

        JSONArray multimedia = jsonObject.getJSONArray("multimedia");
        if(multimedia.length() >0){
            JSONObject multimediaJson = multimedia.getJSONObject(1);
            this.thumbNail = "http://www.nytimes.com/" + multimediaJson.getString("url");
            this.thumbNailHeight = multimediaJson.getInt("height");
            this.thumbNailWidth = multimediaJson.getInt("width");
        }
        else{
            this.thumbNail = "";
        }
    }


    public static ArrayList<Article> fromJSONArray(JSONArray array){

        ArrayList<Article> results = new ArrayList<>();

        for (int x=0;x<array.length();x++){
            try {
                results.add(new Article(array.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.webUrl);
        dest.writeString(this.headline);
        dest.writeString(this.thumbNail);
        dest.writeInt(this.thumbNailHeight);
        dest.writeInt(this.thumbNailWidth);
    }

    protected Article(Parcel in) {
        this.webUrl = in.readString();
        this.headline = in.readString();
        this.thumbNail = in.readString();
        this.thumbNailHeight = in.readInt();
        this.thumbNailWidth = in.readInt();
    }

    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}

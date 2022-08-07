package com.conicskill.app.data.model;

import androidx.annotation.NonNull;

import com.idanatz.oneadapter.external.interfaces.Diffable;

public class VideoList implements Diffable {
    String url;
    String quailty;
    String extension;
    String videoname;
    String videoid;
    String audioUrl;

    public VideoList(String url, String quailty, String extension, String videoname, String videoid, String audioUrl) {
        this.url = url;
        this.quailty = quailty;
        this.extension = extension;
        this.videoname = videoname;
        this.videoid = videoid;
        this.audioUrl =audioUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQuailty() {
        return quailty;
    }

    public void setQuailty(String quailty) {
        this.quailty = quailty;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getVideoname() {
        return videoname;
    }

    public void setVideoname(String videoname) {
        this.videoname = videoname;
    }

    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    @Override
    public String toString() {
        return "{" +
                "url='" + url + '\'' +
                ", quailty='" + quailty + '\'' +
                ", extension='" + extension + '\'' +
                ", videoname='" + videoname + '\'' +
                ", videoid='" + videoid + '\'' +
                ", audioUrl='" + audioUrl + '\'' +
                '}';
    }

    @Override
    public boolean areContentTheSame(@NonNull Object o) {
        return false;
    }

    @Override
    public long getUniqueIdentifier() {
        return System.identityHashCode(this);
    }
}

package com.conicskill.app.util.ytextractor;

public interface CallBacks {
    void callbackObserver(Object object);
    interface playerCallBack{
        void onItemClickOnItem(int albumId);
        void onPlayingEnd();
        void onPlayingBuffering();
        void onPlayingReady();
    }
}
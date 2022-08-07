package com.conicskill.app.data.model.youtubeVideo;

import com.google.gson.annotations.SerializedName;
import com.idanatz.oneadapter.external.interfaces.Diffable;

import org.jetbrains.annotations.NotNull;

public class ItemsItem implements Diffable {

    @SerializedName("snippet")
    private Snippet snippet;

    @SerializedName("kind")
    private String kind;

    @SerializedName("etag")
    private String etag;

    @SerializedName("id")
    private Id id;

    public Snippet getSnippet() {
        return snippet;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return
                "ItemsItem{" +
                        "snippet = '" + snippet + '\'' +
                        ",kind = '" + kind + '\'' +
                        ",etag = '" + etag + '\'' +
                        ",id = '" + id + '\'' +
                        "}";
    }

    @Override
    public boolean areContentTheSame(@NotNull Object o) {
        return false;
    }

    @Override
    public long getUniqueIdentifier() {
        return System.identityHashCode(this);
    }
}
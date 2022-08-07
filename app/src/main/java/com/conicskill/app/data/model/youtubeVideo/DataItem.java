package com.conicskill.app.data.model.youtubeVideo;

import com.google.gson.annotations.SerializedName;
import com.idanatz.oneadapter.external.interfaces.Diffable;

import org.jetbrains.annotations.NotNull;

import java.net.IDN;

public class DataItem implements Diffable {

	@SerializedName("snippet")
	private Snippet snippet;

	@SerializedName("kind")
	private String kind;

	@SerializedName("etag")
	private String etag;

	@SerializedName("id")
	private Id id;

	public Snippet getSnippet(){
		return snippet;
	}

	public String getKind(){
		return kind;
	}

	public String getEtag(){
		return etag;
	}

	public Id getId(){
		return id;
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
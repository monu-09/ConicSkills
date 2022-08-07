package com.conicskill.app.ui.pdf;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.JsonReader;

import androidx.annotation.Nullable;

import com.conicskill.app.data.model.VideoList;
import com.conicskill.app.util.Constants;
import com.conicskill.app.util.ytextractor.DemoUtil;
import com.conicskill.app.util.ytextractor.IntentUtil;
import com.conicskill.app.util.ytextractor.PlaylistGroup;
import com.conicskill.app.util.ytextractor.PlaylistHolder;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.common.collect.ImmutableList;

import org.json.JSONArray;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.android.exoplayer2.util.Assertions.checkNotNull;
import static com.google.android.exoplayer2.util.Assertions.checkState;

public class SampleListLoader extends AsyncTask<String, Void, List<PlaylistGroup>> {

        private boolean sawError;
        private Context mContext;
        private String chatId = "";
        private String chatEnabled = "";
        private String pdfUrl = "";
        private JSONArray arrayMain = null;
        private List<VideoList> videoLists = new ArrayList<>();

        public SampleListLoader(Context context,
                                String chatId,
                                String chatEnabled,
                                String pdfUrl,
                                JSONArray arrayMain,
                                List<VideoList> videoLists) {
            this.mContext = context;
            this.chatId = chatId;
            this.chatEnabled = chatEnabled;
            this.pdfUrl = pdfUrl;
            this.arrayMain = arrayMain;
            this.videoLists = videoLists;
        }

        @Override
        protected List<PlaylistGroup> doInBackground(String... uris) {
            List<PlaylistGroup> result = new ArrayList<>();
            DataSource dataSource =
                    DemoUtil.getDataSourceFactory(mContext).createDataSource();
            try {
                readPlaylistGroups(new JsonReader(new
                        InputStreamReader(convertStringToInputStream(arrayMain.toString()),
                        "UTF-8")), result);
            } catch (Exception e) {
                sawError = true;
            } finally {
                Util.closeQuietly(dataSource);
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<PlaylistGroup> result) {
            Intent intent = new Intent(mContext, PlayerActivity.class);
            intent.putExtra(IntentUtil.PREFER_EXTENSION_DECODERS_EXTRA, false);
            intent.putExtra(Constants.EXTRA_VIDEO_YOUTUBE_ID, "");
            intent.putExtra(Constants.EXTRA_USE_LIVE_CLASS_BOLLEAN, false);
            intent.putExtra(Constants.EXTRA_USE_LIVE_CLASS_ID, "");
            intent.putExtra(Constants.AUDIO, "");
            intent.putExtra(Constants.OFFLINE, false);
            intent.putExtra(Constants.JSON, videoLists.toString());
            intent.putExtra("chatId", chatId);
            intent.putExtra("chatEnabled", chatEnabled);
            intent.putExtra("pdfUrl", pdfUrl);


            IntentUtil.addToIntent(result.get(0).playlists.get(0).mediaItems, intent);
            mContext.startActivity(intent);

        }

        private InputStream convertStringToInputStream(String name) {

            InputStream result = new ByteArrayInputStream(name.getBytes(StandardCharsets.UTF_8));
            return result;

        }

        private void readPlaylistGroups(JsonReader reader, List<PlaylistGroup> groups)
                throws IOException {
            reader.beginArray();
            while (reader.hasNext()) {
                readPlaylistGroup(reader, groups);
            }
            reader.endArray();
        }

        private void readPlaylistGroup(JsonReader reader, List<PlaylistGroup> groups)
                throws IOException {
            String groupName = "";
            ArrayList<PlaylistHolder> playlistHolders = new ArrayList<>();

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "name":
                        groupName = reader.nextString();
                        break;
                    case "samples":
                        reader.beginArray();
                        while (reader.hasNext()) {
                            playlistHolders.add(readEntry(reader, false));
                        }
                        reader.endArray();
                        break;
                    case "_comment":
                        reader.nextString(); // Ignore.
                        break;
                    default:
                        throw new ParserException("Unsupported name: " + name);
                }
            }
            reader.endObject();

            PlaylistGroup group = getGroup(groupName, groups);
            group.playlists.addAll(playlistHolders);
        }


        private PlaylistHolder readEntry(JsonReader reader, boolean insidePlaylist) throws IOException {
            Uri uri = null;
            String extension = null;
            String title = null;
            ArrayList<PlaylistHolder> children = null;
            Uri subtitleUri = null;
            String subtitleMimeType = null;
            String subtitleLanguage = null;

            MediaItem.Builder mediaItem = new MediaItem.Builder();
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "name":
                        title = reader.nextString();
                        break;
                    case "uri":
                        uri = Uri.parse(reader.nextString());
                        break;
                    case "extension":
                        extension = reader.nextString();
                        break;
                    case "clip_start_position_ms":
                        mediaItem.setClipStartPositionMs(reader.nextLong());
                        break;
                    case "clip_end_position_ms":
                        mediaItem.setClipEndPositionMs(reader.nextLong());
                        break;
                    case "ad_tag_uri":
                        mediaItem.setAdTagUri(reader.nextString());
                        break;
                    case "drm_scheme":
                        mediaItem.setDrmUuid(Util.getDrmUuid(reader.nextString()));
                        break;
                    case "drm_license_uri":
                    case "drm_license_url": // For backward compatibility only.
                        mediaItem.setDrmLicenseUri(reader.nextString());
                        break;
                    case "drm_key_request_properties":
                        Map<String, String> requestHeaders = new HashMap<>();
                        reader.beginObject();
                        while (reader.hasNext()) {
                            requestHeaders.put(reader.nextName(), reader.nextString());
                        }
                        reader.endObject();
                        mediaItem.setDrmLicenseRequestHeaders(requestHeaders);
                        break;
                    case "drm_session_for_clear_content":
                        if (reader.nextBoolean()) {
                            mediaItem.setDrmSessionForClearTypes(
                                    ImmutableList.of(C.TRACK_TYPE_VIDEO, C.TRACK_TYPE_AUDIO));
                        }
                        break;
                    case "drm_multi_session":
                        mediaItem.setDrmMultiSession(reader.nextBoolean());
                        break;
                    case "drm_force_default_license_uri":
                        mediaItem.setDrmForceDefaultLicenseUri(reader.nextBoolean());
                        break;
                    case "subtitle_uri":
                        subtitleUri = Uri.parse(reader.nextString());
                        break;
                    case "subtitle_mime_type":
                        subtitleMimeType = reader.nextString();
                        break;
                    case "subtitle_language":
                        subtitleLanguage = reader.nextString();
                        break;
                    case "playlist":
                        checkState(!insidePlaylist, "Invalid nesting of playlists");
                        children = new ArrayList<>();
                        reader.beginArray();
                        while (reader.hasNext()) {
                            children.add(readEntry(reader, /* insidePlaylist= */ true));
                        }
                        reader.endArray();
                        break;
                    default:
                        throw new ParserException("Unsupported attribute name: " + name);
                }
            }
            reader.endObject();

            if (children != null) {
                List<MediaItem> mediaItems = new ArrayList<>();
                for (int i = 0; i < children.size(); i++) {
                    mediaItems.addAll(children.get(i).mediaItems);
                }
                return new PlaylistHolder(title, mediaItems);
            } else {
                @Nullable
                String adaptiveMimeType =
                        Util.getAdaptiveMimeTypeForContentType(Util.inferContentType(uri, extension));
                mediaItem
                        .setUri(uri)
                        .setMediaMetadata(new MediaMetadata.Builder().setTitle(title).build())
                        .setMimeType(adaptiveMimeType);
                if (subtitleUri != null) {
                    MediaItem.Subtitle subtitle =
                            new MediaItem.Subtitle(
                                    subtitleUri,
                                    checkNotNull(
                                            subtitleMimeType, "subtitle_mime_type is required if subtitle_uri is set."),
                                    subtitleLanguage);
                    mediaItem.setSubtitles(Collections.singletonList(subtitle));
                }
                return new PlaylistHolder(title, Collections.singletonList(mediaItem.build()));
            }
        }

        private PlaylistGroup getGroup(String groupName, List<PlaylistGroup> groups) {
            for (int i = 0; i < groups.size(); i++) {
                if (Util.areEqual(groupName, groups.get(i).title)) {
                    return groups.get(i);
                }
            }
            PlaylistGroup group = new PlaylistGroup(groupName);
            groups.add(group);
            return group;
        }
    }
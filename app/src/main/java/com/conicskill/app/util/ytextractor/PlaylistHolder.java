package com.conicskill.app.util.ytextractor;

import com.google.android.exoplayer2.MediaItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.android.exoplayer2.util.Assertions.checkArgument;

public final class PlaylistHolder {

        public final String title;
        public final List<MediaItem> mediaItems;

        public PlaylistHolder(String title, List<MediaItem> mediaItems) {
            checkArgument(!mediaItems.isEmpty());
            this.title = title;
            this.mediaItems = Collections.unmodifiableList(new ArrayList<>(mediaItems));
        }
}
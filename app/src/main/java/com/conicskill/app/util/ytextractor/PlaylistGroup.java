package com.conicskill.app.util.ytextractor;

import java.util.ArrayList;
import java.util.List;

public final class PlaylistGroup {

        public final String title;
        public final List<PlaylistHolder> playlists;

        public PlaylistGroup(String title) {
            this.title = title;
            this.playlists = new ArrayList<>();
        }
    }
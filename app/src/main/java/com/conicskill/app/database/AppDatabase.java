package com.conicskill.app.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.conicskill.app.data.model.exam.Modules;
import com.conicskill.app.data.model.login.TestData;
import com.conicskill.app.data.model.playlist.CategoryListItem;
import com.conicskill.app.data.model.videoCouses.CourseListItem;
import com.conicskill.app.data.model.videoCouses.SubCategoryItem;

@Database(entities = {Modules.class, TestData.class, CategoryListItem.class, CourseListItem.class,
        SubCategoryItem.class, Downloads.class, DownloadedVideo.class}, version = 11, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ModuleDao moduleDao();
}

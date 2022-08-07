package com.conicskill.app.database;

import com.conicskill.app.data.model.exam.Modules;
import com.conicskill.app.data.model.playlist.CategoryListItem;
import com.conicskill.app.data.model.videoCouses.CourseListItem;
import com.conicskill.app.data.model.videoCouses.SubCategoryItem;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class ModuleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<List<Long>> insert(List<Modules> modules);

    @Query("SELECT * FROM MODULES WHERE moduleId IN (:modules)")
    public abstract Single<List<Modules>> fetchModulesFromDB(List<String> modules);

    @Query("UPDATE MODULES SET attemptedQuestionsData =:questionData WHERE moduleId =:moduleID")
    public abstract Single<Integer> updateQuestionData(String questionData, String moduleID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<Long> insertCourse(CourseListItem courseListItem);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<Long> insertCategory(CategoryListItem category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Maybe<Long> insertSubCategory(SubCategoryItem subcategory);

    @Query("SELECT * FROM Courses")
    public abstract Single<List<CourseListItem>> getAllCourses();

    @Query("SELECT * FROM Courses WHERE courseId=:coursesId")
    public abstract Single<List<CourseListItem>> getCourseList(String coursesId);

    @Query("SELECT * FROM Categories WHERE courseId=:courseId")
    public abstract Single<List<CategoryListItem>> getCategoryList(String courseId);

    @Query("SELECT * FROM Subcategory WHERE catId=:catId")
    public abstract Single<List<SubCategoryItem>> getSubCategoryList(String catId);

    @Insert
    public abstract Maybe<Long> insertDownloads(Downloads downloads);

    @Insert
    public abstract Maybe<Long> insertDownloadsVideo(DownloadedVideo downloads);

    @Query("SELECT * FROM Downloads WHERE subCategoryId=:subcategoryId")
    public abstract Single<List<Downloads>> getDownloadsById(String subcategoryId);

    @Query("SELECT * FROM DownloadedVideo WHERE subCategoryId=:subcategoryId")
    public abstract Single<List<DownloadedVideo>> getDownloadedVideoById(String subcategoryId);

    @Query("SELECT * FROM DOWNLOADS WHERE url=:pdfUrl")
    public abstract Single<List<Downloads>> checkWhetherFileExists(String pdfUrl);

    @Query("SELECT * FROM DOWNLOADEDVIDEO WHERE originalUrl=:videoUrl")
    public abstract Single<List<DownloadedVideo>> checkWhetherVideoFileExists(String videoUrl);

    @Delete
    public abstract void deletePdfFile(Downloads download);

    @Delete
    public abstract void deleteVideoFile(DownloadedVideo download);
}
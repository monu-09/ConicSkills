package com.conicskill.app.data.model.playlist;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.conicskill.app.data.model.videoCouses.SubCategoryItem;
import com.google.firebase.encoders.annotations.Encodable;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = "Categories")
public class CategoryListItem implements Serializable {

    @NotNull
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "id")
    private String id;

    @SerializedName("categoryName")
    @ColumnInfo(name = "categoryName")
    private String categoryName;

    @SerializedName("thumbnail")
    @ColumnInfo(name = "thumbnail")
    private String thumbnail;

    @SerializedName("colorCode")
    @ColumnInfo(name = "colorCode")
    private String colorCode;

    @ColumnInfo(name = "courseId")
    private String courseId;

    @SerializedName("subCategory")
    @Ignore
    private ArrayList<SubCategoryItem> subCategoriesArrayList = new ArrayList<>();

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ArrayList<SubCategoryItem> getSubCategoriesArrayList() {
        return subCategoriesArrayList;
    }

    public void setSubCategoriesArrayList(ArrayList<SubCategoryItem> subCategoriesArrayList) {
        this.subCategoriesArrayList = subCategoriesArrayList;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return
                "CategoryListItem{" +
                        "id = '" + id + '\'' +
                        ",categoryName = '" + categoryName + '\'' +
                        "}";
    }
}

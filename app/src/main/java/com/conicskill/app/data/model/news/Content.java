package com.conicskill.app.data.model.news;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="content", strict = false)
public class Content {

    @Attribute(name = "url")
    private String url;

    @Attribute(name = "width", required = false)
    private String width;

    @Attribute(name = "height", required = false)
    private String height;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}

package com.conicskill.app.data.model.exam;

import com.google.gson.annotations.SerializedName;

public class ModuleBodyWrapper {

    @SerializedName("body")
    private ModuleRequest moduleRequest;

    public ModuleRequest getModuleRequest() {
        return moduleRequest;
    }

    public void setModuleRequest(ModuleRequest moduleRequest) {
        this.moduleRequest = moduleRequest;
    }
}

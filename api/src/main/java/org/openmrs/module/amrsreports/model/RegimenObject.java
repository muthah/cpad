package org.openmrs.module.amrsreports.model;

/**
 * wrapper class for regimen
 */
public class RegimenObject {

    private String regimenName;
    private String regimenType;

    public String getRegimenName() {
        return regimenName;
    }

    public void setRegimenName(String regimenName) {
        this.regimenName = regimenName;
    }

    public String getRegimenType() {
        return regimenType;
    }

    public void setRegimenType(String regimenType) {
        this.regimenType = regimenType;
    }
}

package org.nbme.dwbi.synthetic.model;

import org.springframework.stereotype.Component;
/**
 * Model object to contain Application version information.
 */
@Component
public class AppVersion {
    private String version;
    private String buildDate;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(String buildDate) {
        this.buildDate = buildDate;
    }
}


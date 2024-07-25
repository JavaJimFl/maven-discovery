
package org.kaib.discover.maven;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Dependency {

    private String artifactId;
    private String classifier;
    private String groupId;
    private String scope;
    private String type;
    private boolean transitive;
    private String version;

    public String getArtifactId() {
        return this.artifactId;
    }

    public void setArtifactId(String newArtifactId) {
        this.artifactId = newArtifactId;
    }

    public String getClassifier() {
        return this.classifier;
    }

    public void setClassifier(String newClassifier) {
        this.classifier = newClassifier;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String newGroupId) {
        this.groupId = newGroupId;
    }

    public String getScope() {
        return this.scope;
    }

    public void setScope(String newScope) {
        this.scope = newScope;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String newType) {
        this.type = newType;
    }

    public boolean isTransitive() {
        return this.transitive;
    }

    public void setTransitive(boolean newTransitive) {
        this.transitive = newTransitive;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String newVersion) {
        this.version = newVersion;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("artifactId", this.artifactId)
                .append("classifier", this.classifier)
                .append("groupId", this.groupId)
                .append("scope", this.scope)
                .append("type", this.type)
                .append("transitive", this.transitive)
                .append("version", this.version)
                .toString();
    }
}

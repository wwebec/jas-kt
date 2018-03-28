package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import java.util.Objects;

public class Owner {

    private String userId;

    private String avgId;

    private String accessToken;

    protected Owner() {
        // For reflection libs
    }

    public Owner(Builder builder) {
        this.userId = builder.userId;
        this.avgId = builder.avgId;
        this.accessToken = builder.accessToken;
    }

    public String getUserId() {
        return userId;
    }

    public String getAvgId() {
        return avgId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Owner owner = (Owner) o;
        return Objects.equals(userId, owner.userId) &&
                Objects.equals(avgId, owner.avgId) &&
                Objects.equals(accessToken, owner.accessToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, avgId, accessToken);
    }

    @Override
    public String toString() {
        return "Owner{" +
                "userId='" + userId + '\'' +
                ", avgId='" + avgId + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }

    public static final class Builder {
        private String userId;
        private String avgId;
        private String accessToken;

        public Builder() {
        }

        public Owner build() {
            return new Owner(this);
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setAvgId(String avgId) {
            this.avgId = avgId;
            return this;
        }

        public Builder setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }
    }
}

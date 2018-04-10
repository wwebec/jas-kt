package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import java.util.Objects;

public class Owner {

    private String userId;

    private String companyId;

    private String accessToken;

    protected Owner() {
        // For reflection libs
    }

    public Owner(Builder builder) {
        this.userId = builder.userId;
        this.companyId = builder.companyId;
        this.accessToken = builder.accessToken;
    }

    public String getUserId() {
        return userId;
    }

    public String getCompanyId() {
        return companyId;
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
                Objects.equals(companyId, owner.companyId) &&
                Objects.equals(accessToken, owner.accessToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, companyId, accessToken);
    }

    @Override
    public String toString() {
        return "Owner{" +
                "userId='" + userId + '\'' +
                ", companyId='" + companyId + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }

    public static final class Builder {
        private String userId;
        private String companyId;
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

        public Builder setCompanyId(String companyId) {
            this.companyId = companyId;
            return this;
        }

        public Builder setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }
    }
}

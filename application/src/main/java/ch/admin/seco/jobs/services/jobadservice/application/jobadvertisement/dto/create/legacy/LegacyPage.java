package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

public class LegacyPage {
    private int size;
    private long totalElements;
    private int totalPage;
    private int number;

    protected LegacyPage() {
        // For reflection libs
    }

    public LegacyPage(int size, long totalElements, int totalPage, int number) {
        this.size = size;
        this.totalElements = totalElements;
        this.totalPage = totalPage;
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getNumber() {
        return number;
    }
}

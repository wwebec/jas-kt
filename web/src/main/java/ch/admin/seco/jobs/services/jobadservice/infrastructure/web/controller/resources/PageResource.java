package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller.resources;

import org.springframework.data.domain.Page;

import java.util.List;

public class PageResource<T> {

    private List<T> content;
    private int totalElements;
    private int totalPages;
    private int currentPage;
    private int currentSize;
    private boolean first;
    private boolean last;

    public static <T> PageResource<T> of(Page<T> page) {
        return new PageResource<>(
                page.getContent(),
                (int) page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.isFirst(),
                page.isLast()
        );
    }

    public PageResource(List<T> content, int totalElements, int totalPages, int currentPage, int currentSize, boolean first, boolean last) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.currentSize = currentSize;
        this.first = first;
        this.last = last;
    }

    public List<T> getContent() {
        return content;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public boolean isFirst() {
        return first;
    }

    public boolean isLast() {
        return last;
    }
}

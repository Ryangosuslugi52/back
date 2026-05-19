package com.example.dto;

import com.example.domain.Task;
import io.micronaut.serde.annotation.Serdeable;
import java.util.List;

@Serdeable
public class PaginatedTasksResponse {
    private List<Task> items;
    private Meta meta;

    public PaginatedTasksResponse(List<Task> items, long page, long limit, long total) {
        this.items = items;
        this.meta = new Meta(page, limit, total);
    }

    public List<Task> getItems() { return items; }
    public Meta getMeta() { return meta; }

    @Serdeable
    public static class Meta {
        private long page;
        private long limit;
        private long total;

        public Meta(long page, long limit, long total) {
            this.page = page;
            this.limit = limit;
            this.total = total;
        }
        public long getPage() {
            return page;
        }

        public long getLimit() {
            return limit;
        }

        public long getTotal() {
            return total;
        }
    }
}
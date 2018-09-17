package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.dlq;


import java.util.NoSuchElementException;

import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Endpoint(id = "dlq-item")
public class DLQItemActuatorEndpoint {

    private final DLQItemService dlqItemService;

    public DLQItemActuatorEndpoint(DLQItemService dlqItemService) {
        this.dlqItemService = dlqItemService;
    }

    @ReadOperation
    public Page<DLQItem> getDLQItemList(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "errorTime"));
        return this.dlqItemService.getItems(pageRequest);
    }

    @ReadOperation
    public DLQItem getOne(@Selector String id) {
        return this.dlqItemService.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @DeleteOperation
    public void delete(@Selector String id) {
        this.dlqItemService.delete(id);
    }

}

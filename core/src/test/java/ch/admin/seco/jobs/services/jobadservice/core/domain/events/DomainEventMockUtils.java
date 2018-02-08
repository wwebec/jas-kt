package ch.admin.seco.jobs.services.jobadservice.core.domain.events;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class DomainEventMockUtils {

    private final DomainEventPublisher domainEventPublisher;

    public DomainEventMockUtils() {
        domainEventPublisher = mock(DomainEventPublisher.class);
        DomainEventPublisher.set(domainEventPublisher);
    }

    public DomainEventPublisher getDomainEventPublisher() {
        return domainEventPublisher;
    }

    public <T extends DomainEvent> T assertSingleDomainEventPublished(DomainEventType domainEventType) {
        ArgumentCaptor<DomainEvent> domainEventArgumentCaptor = ArgumentCaptor.forClass(DomainEvent.class);
        verify(domainEventPublisher, times(1)).publishEvent(domainEventArgumentCaptor.capture());
        assertThat(domainEventArgumentCaptor.getValue().getDomainEventType()).isEqualTo(domainEventType);
        return (T) domainEventArgumentCaptor.getValue();
    }

    public void verifyNoEventsPublished(){
        verify(domainEventPublisher, never()).publishEvent(any());
    }

    public void clearEvents() {
        Mockito.reset(domainEventPublisher);
    }

}
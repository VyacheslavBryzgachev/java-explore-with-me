package ru.practicum.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.enums.Status;

import java.util.List;

@Builder
@Data
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private Status status;
}

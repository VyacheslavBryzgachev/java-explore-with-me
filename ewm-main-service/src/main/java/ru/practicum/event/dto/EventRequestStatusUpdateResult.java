package ru.practicum.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

@Data
@Builder
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}

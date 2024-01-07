package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.searchParams.SearchEventParams;
import ru.practicum.event.searchParams.SearchEventParamsAdmin;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    List<EventShortDto> getUserEvents(Long id, Integer from, Integer size);

    EventFullDto createEvent(Long id, NewEventDto newEventDto);

    EventFullDto getEvent(Long id, Long eventId);

    EventFullDto updateEvent(Long id, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getRequests(Long id, Long eventId);

    EventRequestStatusUpdateResult updateRequests(Long id, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    EventFullDto getEventById(Long eventId, HttpServletRequest request);

    EventFullDto adminUpdateEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventShortDto> publicGetAllEvents(SearchEventParams searchEventParams, HttpServletRequest request);

    List<EventFullDto> adminGetAllEvents(SearchEventParamsAdmin searchEventParamsAdmin);
}

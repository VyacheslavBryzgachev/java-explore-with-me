package ru.practicum.event.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.category.dao.DbCategoryStorage;
import ru.practicum.category.model.Category;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.StatDtoRequest;
import ru.practicum.dto.StatDtoStatResponse;
import ru.practicum.enums.State;
import ru.practicum.enums.StateAction;
import ru.practicum.enums.Status;
import ru.practicum.event.dao.DbEventStorage;
import ru.practicum.event.dao.DbLocationStorage;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.event.searchParams.SearchEventParams;
import ru.practicum.event.searchParams.SearchEventParamsAdmin;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.WrongRequestException;
import ru.practicum.exceptions.WrongTimeException;
import ru.practicum.request.dao.DbRequestStorage;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.user.dao.DbUserStorage;
import ru.practicum.user.model.User;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventsServiceImpl implements EventService {

    private final DbEventStorage dbEventStorage;
    private final DbRequestStorage dbRequestStorage;
    private final DbLocationStorage dbLocationStorage;
    private final DbCategoryStorage dbCategoryStorage;
    private final DbUserStorage dbUserStorage;
    private final StatsClient statsClient;
    private final ObjectMapper objectMapper;
    private final EventMapper eventMapper = new EventMapper();
    private final RequestMapper requestMapper = new RequestMapper();

    @Value("${server.application.name:ewm-service}")
    private String app;


    @Override
    public List<EventShortDto> getUserEvents(Long id, Integer from, Integer size) {
        Pageable pageWithFromAndSize = PageRequest.of(from, size);
        return dbEventStorage.getUserEvents(id, pageWithFromAndSize).stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto createEvent(Long id, NewEventDto newEventDto) {
        if (newEventDto.getEventDate().minusHours(2).equals(LocalDateTime.now())) {
            throw new WrongRequestException(
                    "Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента");
        }
        User user = dbUserStorage.getById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Category category = dbCategoryStorage.getById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));
        Location location = dbLocationStorage.getById(newEventDto.getLocation().getId())
                .orElse(dbLocationStorage.create(newEventDto.getLocation()));
        Event event = eventMapper.toEvent(newEventDto);
        event.setInitiator(user);
        event.setCategory(category);
        event.setLocation(location);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(State.PENDING);
        dbEventStorage.createEvent(event);
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto getEvent(Long id, Long eventId) {
        Event event = dbEventStorage.getEvent(id, eventId).stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto updateEvent(Long id, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        if (updateEventUserRequest.getEventDate() != null) {
            if (updateEventUserRequest.getEventDate().minusHours(2).equals(LocalDateTime.now())) {
                throw new WrongRequestException(
                        "Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента");
            }
        }
        if (updateEventUserRequest.getEventDate() != null && updateEventUserRequest.getEventDate().isBefore(LocalDateTime.now())) {
            throw new WrongTimeException("Нельзя изменить дату события на уже наступившую");
        }
        Event event = dbEventStorage.getEvent(id, eventId).stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        if (event.getState().equals(State.PUBLISHED)) {
            throw new WrongRequestException("Нельзя редактировать опубликованное событие");
        }
        if (updateEventUserRequest.getAnnotation() != null && !updateEventUserRequest.getAnnotation().isBlank()) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }
        if (updateEventUserRequest.getCategory() != null) {
            event.setCategory(dbCategoryStorage.getById(updateEventUserRequest.getCategory()).stream()
                    .findFirst().orElseThrow(() -> new NotFoundException("Категория не найдена")));
        }
        if (updateEventUserRequest.getDescription() != null && !updateEventUserRequest.getDescription().isBlank()) {
            event.setDescription(updateEventUserRequest.getDescription());
        }
        if (updateEventUserRequest.getEventDate() != null) {
            event.setEventDate(updateEventUserRequest.getEventDate());
        }
        if (updateEventUserRequest.getLocation() != null) {
            event.setLocation(updateEventUserRequest.getLocation());
        }
        if (updateEventUserRequest.getPaid() != null) {
            event.setPaid(updateEventUserRequest.getPaid());
        }
        if (updateEventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }
        if (updateEventUserRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }
        if (updateEventUserRequest.getTitle() != null && !updateEventUserRequest.getTitle().isBlank()) {
            event.setTitle(updateEventUserRequest.getTitle());
        }
        if (updateEventUserRequest.getStateAction() != null) {
            if (updateEventUserRequest.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
                event.setState(State.PENDING);
            } else if (updateEventUserRequest.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
                event.setState(State.CANCELED);
            }
        }
        return eventMapper.toEventFullDto(dbEventStorage.createEvent(event));
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        dbUserStorage.getById(userId).stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return dbRequestStorage.getRequest(eventId).stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateRequests(Long id, Long eventId,
                                                         EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        Event event = dbEventStorage.getEvent(id, eventId).stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            throw new WrongRequestException("Это событие не требует подтверждения запросов");
        }
        Status status = eventRequestStatusUpdateRequest.getStatus();
        Integer countConfReq = dbRequestStorage.getConfirmedRequestsByEventId(eventId);
        if (event.getParticipantLimit().equals(countConfReq)) {
            throw new WrongRequestException("Лимит участников исчерпан");
        }
        List<Request> requests = dbRequestStorage.getRequestsByIds(eventRequestStatusUpdateRequest.getRequestIds());
        for (Request request : requests) {
            if (request.getStatus().equals(Status.CONFIRMED) && status.equals(Status.REJECTED)) {
                throw new WrongRequestException("Нельзя отклонить подтвержденную заявку");
            } else {
                request.setStatus(status);
                dbRequestStorage.createRequest(request);
            }
        }
        List<ParticipationRequestDto> confirmedRequests = requests.stream()
                .filter(request -> request.getStatus().equals(Status.CONFIRMED))
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
        List<ParticipationRequestDto> rejectedRequests = requests.stream()
                .filter(request -> request.getStatus().equals(Status.REJECTED))
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }

    @Override
    public EventFullDto getEventById(Long eventId, HttpServletRequest request) {
        Event event = dbEventStorage.getEventById(eventId).stream()
                .findFirst()
                .filter(event1 -> event1.getState().equals(State.PUBLISHED))
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        addStatsClient(request);
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        Map<Long, Long> viewStatsMap = getViewsAllEvents(List.of(event));
        Long views = viewStatsMap.getOrDefault(event.getId(), 0L);
        eventFullDto.setViews(views + 1);
        return eventFullDto;
    }

    @Override
    public EventFullDto adminUpdateEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = dbEventStorage.getEvent(eventId).stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new WrongRequestException("Дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
        }
        if (updateEventAdminRequest.getEventDate() != null && updateEventAdminRequest.getEventDate().isBefore(LocalDateTime.now())) {
            throw new WrongTimeException("Нельзя изменить дату события на уже наступившую");
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            if (!updateEventAdminRequest.getPaid().equals(event.getPaid())) {
                event.setPaid(updateEventAdminRequest.getPaid());
            }
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            if (!event.getState().equals(State.PENDING)) {
                throw new WrongRequestException("Нельзя опубликовать событие которое уже в статусе опубликовано");
            }
            if (updateEventAdminRequest.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (updateEventAdminRequest.getStateAction().equals(StateAction.REJECT_EVENT)) {
                event.setState(State.CANCELED);
            }
        }
        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        return eventMapper.toEventFullDto(dbEventStorage.createEvent(event));
    }

    @Override
    public List<EventShortDto> publicGetAllEvents(SearchEventParams searchEventParams, HttpServletRequest request) {
        if (searchEventParams.getRangeEnd() != null && searchEventParams.getRangeStart() != null) {
            if (searchEventParams.getRangeEnd().isBefore(searchEventParams.getRangeStart())) {
                throw new WrongTimeException("Дата окончания не может быть раньше даты начала");
            }
        }
        Pageable pageable = PageRequest.of(searchEventParams.getFrom() / searchEventParams.getSize(), searchEventParams.getSize());
        Specification<Event> specification = Specification.where(null);
        LocalDateTime now = LocalDateTime.now();

        if (searchEventParams.getText() != null) {
            String searchText = searchEventParams.getText().toLowerCase();
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), "%" + searchText + "%"),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + searchText + "%")
                    ));
        }

        if (searchEventParams.getCategories() != null && !searchEventParams.getCategories().isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("category").get("id").in(searchEventParams.getCategories()));
        }

        LocalDateTime startDateTime = Objects.requireNonNullElse(searchEventParams.getRangeStart(), now);
        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("eventDate"), startDateTime));

        if (searchEventParams.getRangeEnd() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get("eventDate"), searchEventParams.getRangeEnd()));
        }

        if (searchEventParams.getOnlyAvailable() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("participantLimit"), 0));
        }

        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("state"), State.PUBLISHED));

        List<Event> resultEvents = dbEventStorage.publicGetAllEvents(specification, pageable).getContent();

        List<EventShortDto> result = resultEvents
                .stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
        Map<Long, Long> viewStatsMap = getViewsAllEvents(resultEvents);

        for (EventShortDto event : result) {
            Long viewsFromMap = viewStatsMap.getOrDefault(event.getId(), 0L);
            event.setViews(viewsFromMap);
        }
        return result;
    }

    @Override
    public List<EventFullDto> adminGetAllEvents(SearchEventParamsAdmin searchEventParamsAdmin) {
        PageRequest pageable = PageRequest.of(searchEventParamsAdmin.getFrom() / searchEventParamsAdmin.getSize(),
                searchEventParamsAdmin.getSize());
        Specification<Event> specification = Specification.where(null);

        List<Long> users = searchEventParamsAdmin.getUsers();
        List<State> states = searchEventParamsAdmin.getStates();
        List<Long> categories = searchEventParamsAdmin.getCategories();
        LocalDateTime rangeEnd = searchEventParamsAdmin.getRangeEnd();
        LocalDateTime rangeStart = searchEventParamsAdmin.getRangeStart();

        if (users != null && !users.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("initiator").get("id").in(users));
        }
        if (states != null && !states.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("state").as(State.class).in(states));
        }
        if (categories != null && !categories.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("category").get("id").in(categories));
        }
        if (rangeEnd != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
        }
        if (rangeStart != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
        }
        Page<Event> events = dbEventStorage.adminGetAllEvents(specification, pageable);

        List<EventFullDto> result = events.getContent()
                .stream()
                .map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());

        Map<Long, List<Request>> confirmedRequestsCountMap = getConfirmedRequestsCount(events.toList());
        for (EventFullDto event : result) {
            List<Request> requests = confirmedRequestsCountMap.getOrDefault(event.getId(), List.of());
            event.setConfirmedRequests(requests.size());
        }
        return result;
    }


    private Map<Long, List<Request>> getConfirmedRequestsCount(List<Event> events) {
        List<Request> requests = dbRequestStorage.getConfirmedRequestsByEventIds(events
                .stream().map(Event::getId).collect(Collectors.toList()));
        return requests.stream().collect(Collectors.groupingBy(r -> r.getEvent().getId()));
    }

    private void addStatsClient(HttpServletRequest request) {
        statsClient.createStat(StatDtoRequest.builder()
                .app(app)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().toString())
                .build());
    }

    private Map<Long, Long> getViewsAllEvents(List<Event> events) {
        List<String> uris = events.stream()
                .map(event -> String.format("/events/%s", event.getId()))
                .collect(Collectors.toList());

        List<LocalDateTime> startDates = events.stream()
                .map(Event::getCreatedOn)
                .collect(Collectors.toList());
        LocalDateTime earliestDate = startDates.stream()
                .min(LocalDateTime::compareTo)
                .orElse(null);
        Map<Long, Long> viewStatsMap = new HashMap<>();

        if (earliestDate != null) {
            ResponseEntity<Object> response = statsClient.getStats(earliestDate, LocalDateTime.now(),
                    uris, true);

            List<StatDtoStatResponse> statDtoStatResponses = objectMapper.convertValue(response.getBody(), new TypeReference<>() {
            });

            viewStatsMap = statDtoStatResponses.stream()
                    .filter(statsDto -> statsDto.getUri().startsWith("/events/"))
                    .collect(Collectors.toMap(
                            statsDto -> Long.parseLong(statsDto.getUri().substring("/events/".length())),
                            StatDtoStatResponse::getHits
                    ));
        }
        return viewStatsMap;
    }
}


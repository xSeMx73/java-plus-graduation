package ru.practicum.dto.event.event;

import lombok.*;
import ru.practicum.dto.request.RequestState;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;

    private RequestState status;
}

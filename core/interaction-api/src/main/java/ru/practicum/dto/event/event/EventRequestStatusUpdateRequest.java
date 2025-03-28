package ru.practicum.dto.event.event;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;

    private String status;
}

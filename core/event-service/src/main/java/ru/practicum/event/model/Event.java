package ru.practicum.event.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.category.model.Category;
import ru.practicum.dto.event.event.EventState;
import ru.practicum.dto.event.event.Location;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@ToString
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "initiator_id", nullable = false)
    private Long initiator;

    private String title;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private LocalDateTime eventDate;

    @Embedded
    private Location location;
    @Size(max = 2500)
    private String annotation;
    @Size(max = 7500)
    private String description;

    private long participantLimit;

    private Boolean paid;

    private Boolean requestModeration;

    @Column(name = "confirmed_requests")
    private Long confirmedRequests;

    @Column(name = "views")
    private Long views;

    private LocalDateTime createdOn;

    private LocalDateTime publishedOn;

    @Enumerated(EnumType.STRING)
    private EventState state;

}

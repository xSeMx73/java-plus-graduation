package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.request.RequestState;
import ru.practicum.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterAndEventAndStatusNotLike(long userId, long eventId, RequestState status);

    List<Request> findAllByRequester(long userId);

    List<Request> findAllByRequesterAndEvent(Long userId, Long eventId);


    List<Request> findAllByEvent(Long eventId);
}

package ru.practicum.feign;


import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.dto.user.UserShortDto;

import java.util.List;


@FeignClient(name = "user-service", path = "/admin/users")
public interface UserFeignClient {

    @GetMapping("/short")
    List<UserShortDto> findShortUsers(@RequestParam List<Long> ids) throws FeignException;

    @GetMapping("/valid")
    void validateUser(@RequestParam Long userId) throws FeignException;

}

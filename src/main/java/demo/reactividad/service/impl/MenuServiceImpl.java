package demo.reactividad.service.impl;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.reactividad.dto.request.MenuCreateRequestDTO;
import demo.reactividad.dto.response.MenuResponseDTO;
import demo.reactividad.enums.MenuCodeException;
import demo.reactividad.exception.menu.MenuNotFoundException;
import demo.reactividad.exception.menu.MenuUnavailableException;
import demo.reactividad.mapper.FoodTypeMapper;
import demo.reactividad.mapper.MenuMapper;
import demo.reactividad.repository.FoodTypeRepository;
import demo.reactividad.repository.MenuRepository;
import demo.reactividad.service.MenuService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
@AllArgsConstructor
@Slf4j
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final FoodTypeRepository foodTypeRepository;
    private final FoodTypeMapper foodTypeMapper;
    private final MenuMapper menuMapper;
    private final Sinks.Many<MenuResponseDTO> sink;

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "menu-service-reactivo", fallbackMethod = "fallbackGetMenu")
    public Mono<MenuResponseDTO> getMenu(UUID menuId) {

        return menuRepository.findById(menuId)
                .switchIfEmpty(Mono.error(() -> new MenuNotFoundException("Menu with id " + menuId + " not found",
                        MenuCodeException.NOT_FOUND.name())))
                .flatMap(menu -> {
                    return foodTypeRepository.findFoodTypeById(menuId)
                            .map(foodType -> foodTypeMapper.toFoodTypeResponseDTO(foodType))
                            .collect(Collectors.toSet())
                            .map(foodTypeDtos -> menuMapper.toMenuResponseDTO(menu, foodTypeDtos));

                });

    }

    public Mono<MenuUnavailableException> fallbackGetMenu() {
        return Mono.just(new MenuUnavailableException("Service unavailable",
                MenuCodeException.UNAVAILABLE.name()));
    }

    @Override
    @Transactional
    public Mono<MenuResponseDTO> createMenu(MenuCreateRequestDTO request) {
        return Mono.just(request)
                .map(r -> menuMapper.toMenu(r))
                .flatMap(menu -> menuRepository.save(menu))
                .map(response -> menuMapper.toMenuResponseDTO(response, Set.of()))
                .doOnNext(this.sink::tryEmitNext);
    }

    @Override
    public Flux<MenuResponseDTO> getMenuStream() {
        return this.sink.asFlux();
    }

    @Override
    @Transactional
    public Mono<Void> deleteMenu(UUID menuId) {
        return this.menuRepository
                .deleteById(menuId)
                .switchIfEmpty(Mono.error(() -> new MenuNotFoundException("Menu with id " + menuId + " not found",
                        MenuCodeException.NOT_FOUND.name())));
    }

    @Override
    public Flux<MenuResponseDTO> create1MillionMenu(Flux<MenuCreateRequestDTO> request) {
        return request
                .doOnNext(r -> log.info("Recieved {}", r))
                .map(r -> menuMapper.toMenu(r))
                .buffer(500)
                .flatMap(menu -> {
                    return menuRepository.saveAll(menu)
                            .onErrorResume(error -> {
                                log.error("Error saving menus list: {}", menu);
                                return Flux.just();
                            });
                })
                .map(response -> menuMapper.toMenuResponseDTO(response, Set.of()))
                .delayElements(Duration.ofSeconds(2));
    }

}

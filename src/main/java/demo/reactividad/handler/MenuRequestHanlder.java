package demo.reactividad.handler;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import demo.reactividad.dto.request.MenuCreateRequestDTO;
import demo.reactividad.dto.response.MenuResponseDTO;
import demo.reactividad.service.MenuService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor

public class MenuRequestHanlder {
    private final MenuService menuService;

    public Mono<ServerResponse> getMenu (ServerRequest request) {
        UUID menuId = UUID.fromString(request.pathVariable("menuId"));
        return this.menuService.getMenu(menuId)
                               .flatMap(r -> ServerResponse.status(HttpStatus.OK).bodyValue(r));
            
    }

    public Mono<ServerResponse> createMenu (ServerRequest request) {
        return request.bodyToMono(MenuCreateRequestDTO.class)
                      .flatMap(dto -> this.menuService.createMenu(dto))
                      .flatMap(responseDto -> ServerResponse.status(HttpStatus.CREATED).bodyValue(responseDto));
            
    }

    public Mono<ServerResponse> getMenuStream(ServerRequest request) {
        Flux<MenuResponseDTO> responseFlux = this.menuService.getMenuStream();
        return ServerResponse.status(HttpStatus.OK)
                             .contentType(MediaType.TEXT_EVENT_STREAM)
                             .body(responseFlux, MenuResponseDTO.class);
    }

    public Mono<ServerResponse> deleteMenu (ServerRequest request) {
        return this.menuService.deleteMenu(UUID.fromString(request.pathVariable("menuId")))
                               .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> create1MillionMenu (ServerRequest request) {
        Flux<MenuResponseDTO> responseFlux = this.menuService.create1MillionMenu(request.bodyToFlux(MenuCreateRequestDTO.class));                                                             
        return ServerResponse.status(HttpStatus.ACCEPTED)
                             .contentType(MediaType.APPLICATION_NDJSON)
                             .body(responseFlux, MenuResponseDTO.class);
    }
}

// package demo.reactividad.controller;

// import java.util.UUID;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import demo.reactividad.dto.request.MenuCreateRequestDTO;
// import demo.reactividad.dto.response.MenuResponseDTO;
// import demo.reactividad.service.MenuService;
// import lombok.RequiredArgsConstructor;
// import reactor.core.publisher.Mono;

// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;

// @RestController
// @RequestMapping("api/v1/menu/")
// @RequiredArgsConstructor
// @CrossOrigin

// public class MenuController {

//     private final MenuService menuService;

//     @GetMapping("/{menuId}")
//     public Mono<ResponseEntity<MenuResponseDTO>> getMenu(@PathVariable UUID menuId ) {
//         return menuService.getMenu(menuId)
//                 .map(response -> ResponseEntity.status(HttpStatus.OK).body(response));
//     }

//     @PostMapping()
//     public Mono<ResponseEntity<MenuResponseDTO>> createMenu(@RequestBody MenuCreateRequestDTO request ) {
//         return menuService.createMenu(request)
//                 .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
//     }
    
// }

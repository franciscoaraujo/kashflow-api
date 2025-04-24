package br.com.bitewisebytes.kashflowapi.api.v1.controller;


import br.com.bitewisebytes.kashflowapi.domain.model.entity.Wallet;
import br.com.bitewisebytes.kashflowapi.domain.validation.UserRequestValidation;
import br.com.bitewisebytes.kashflowapi.dto.request.UserResquestDto;
import br.com.bitewisebytes.kashflowapi.dto.response.UserResponseDto;
import br.com.bitewisebytes.kashflowapi.dto.response.UserResponseLastWalletDto;
import br.com.bitewisebytes.kashflowapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kashflow/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/check")
    public ResponseEntity<String> checkHealth() {
        return ResponseEntity.ok("API KASHFLOW HEALTH - OK");
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponseLastWalletDto> createWallet(@RequestBody UserResquestDto userDto) {
        UserRequestValidation.validate(userDto);
        if (userDto == null) {
            return ResponseEntity.badRequest().body(null);
        }
        UserResponseLastWalletDto userResponseLastWalletDto = userService.createUser(userDto);

        return ResponseEntity.ok().body(userResponseLastWalletDto);
    }

    @GetMapping("/find/email/{email}")
    public ResponseEntity<UserResponseDto> findUserByEmail(@PathVariable String email) {
        UserResponseDto userByEmail = userService.getUserByEmail(email);
        if (userByEmail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(userByEmail);
    }
    @GetMapping("/find/document/{document}")
    public ResponseEntity<UserResponseDto> findUserByDocument(@PathVariable String document) {
        UserResponseDto userByDocument = userService.getUserByDocument(document);
        if (userByDocument == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(userByDocument);
    }

}

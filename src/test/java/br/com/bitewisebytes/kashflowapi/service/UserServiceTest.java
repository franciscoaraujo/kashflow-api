package br.com.bitewisebytes.kashflowapi.service;

import br.com.bitewisebytes.kashflowapi.domain.exceptions.UserException;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.User;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.Wallet;
import br.com.bitewisebytes.kashflowapi.domain.repository.UserRepository;
import br.com.bitewisebytes.kashflowapi.dto.request.UserResquestDto;
import br.com.bitewisebytes.kashflowapi.dto.response.UserResponseDto;
import br.com.bitewisebytes.kashflowapi.dto.response.UserResponseLastWalletDto;
import br.com.bitewisebytes.kashflowapi.dto.response.WalletResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateUser_NewUser() {
        // Arrange
        UserResquestDto userRequestDto = new UserResquestDto("John Doe", "john.doe@example.com", "123456789");
        User user = User.builder()
                .name(userRequestDto.name())
                .email(userRequestDto.email())
                .documentNumber(userRequestDto.document())
                .build();

        when(userRepository.findByEmailAndDocumentNumber(userRequestDto.email(), userRequestDto.document()))
                .thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto userDto = new UserResponseDto( user.getName(), user.getEmail(), user.getDocumentNumber(),user.getWallets());
        when(userRepository.save(any(User.class))).thenReturn(user);

        WalletResponseDto walletResponseDto = new WalletResponseDto(1L, "WALLET123",userDto, BigDecimal.ZERO, LocalDateTime.now());
        when(walletService.createWallet(any(User.class))).thenReturn(walletResponseDto);

        // Act
        UserResponseLastWalletDto response = userService.createUser(userRequestDto);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(walletService).createWallet(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals(userRequestDto.name(), capturedUser.getName());
        assertEquals(userRequestDto.email(), capturedUser.getEmail());
        assertEquals(userRequestDto.document(), capturedUser.getDocumentNumber());
        assertNotNull(response);
    }


    @Test
    void testGetUserByEmail_UserExists() {
        String email = "john.doe@example.com";
        User user = User.builder()
                .name("John Doe")
                .email(email)
                .documentNumber("123456789")
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserResponseDto response = userService.getUserByEmail(email);

        assertNotNull(response);
        assertEquals("John Doe", response.name());
        assertEquals(email, response.email());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testGetUserByEmail_UserNotFound() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUserByEmail(email));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testUpdateUser_UserExists() {
        Long userId = 1L;
        UserResquestDto userRequest = new UserResquestDto("Jane Doe", "jane.doe@example.com", "987654321");
        User existingUser = User.builder()
                .id(userId)
                .name("John Doe")
                .email("john.doe@example.com")
                .documentNumber("123456789")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        UserResponseDto response = userService.updateUser(userId, userRequest);

        assertNotNull(response);
        assertEquals("Jane Doe", response.name());
        assertEquals("jane.doe@example.com", response.email());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        Long userId = 1L;
        UserResquestDto userRequest = new UserResquestDto("Jane Doe", "jane.doe@example.com", "987654321");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.updateUser(userId, userRequest));
        assertEquals("Error updating user", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }
}
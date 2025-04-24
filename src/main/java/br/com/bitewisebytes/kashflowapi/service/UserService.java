package br.com.bitewisebytes.kashflowapi.service;


import br.com.bitewisebytes.kashflowapi.domain.exceptions.UserException;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.User;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.Wallet;
import br.com.bitewisebytes.kashflowapi.domain.repository.UserRepository;
import br.com.bitewisebytes.kashflowapi.dto.request.UserResquestDto;
import br.com.bitewisebytes.kashflowapi.dto.response.UserResponseDto;
import br.com.bitewisebytes.kashflowapi.dto.response.UserResponseLastWalletDto;
import br.com.bitewisebytes.kashflowapi.dto.response.WalletResponseDto;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final WalletService walletService;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, WalletService walletService) {
        this.userRepository = userRepository;
        this.walletService = walletService;
    }


    @Transactional
    public UserResponseLastWalletDto createUser(UserResquestDto userRequest) {
        Optional<User> existingUser = userRepository.findByEmailAndDocumentNumber(userRequest.email(), userRequest.document());

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
            log.info("User already exists, creating a new wallet for user: {}", user.getEmail());
        } else {
            user = User.builder()
                    .name(userRequest.name())
                    .email(userRequest.email())
                    .documentNumber(userRequest.document())
                    .build();
            userRepository.save(user);
        }
        WalletResponseDto walletResponse;
        try {
            walletResponse = walletService.createWallet(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create wallet for user", e);
        }

        return new UserResponseLastWalletDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getDocumentNumber()
        );
    }

    @Transactional
    public UserResponseLastWalletDto createUser__(UserResquestDto userResquestDto) {
        try {
            log.info("Creating user: {}", userResquestDto);

            if (userResquestDto == null) {
                throw new IllegalArgumentException("UserResquestDto cannot be null");
            }

            User user = userRepository.findByEmailAndDocumentNumber(userResquestDto.email(), userResquestDto.document())
                    .orElseGet(() -> {
                        User newUser = User.builder()
                                .name(userResquestDto.name())
                                .email(userResquestDto.email())
                                .documentNumber(userResquestDto.document())
                                .build();
                        log.info("Creating new user: {}", newUser);

                        // Create wallet before saving the user
                        WalletResponseDto walletDto = walletService.createWallet(newUser);
                        if (walletDto != null) {
                            Wallet walletUnit = new Wallet();
                            walletUnit.setId(walletDto.id());
                            walletUnit.setWalletNumber(walletDto.walletNumber());
                            walletUnit.setUser(newUser);
                            walletUnit.setBalance(BigDecimal.ZERO);
                            walletUnit.setDateTimeCreated(walletDto.dataTimeCreated());

                            if (newUser.getWallets() == null) {
                                newUser.setWallets(new ArrayList<>());
                            }
                            newUser.getWallets().add(walletUnit);
                        } else {
                            log.error("Failed to create wallet for user: {}", userResquestDto);
                            throw new RuntimeException("Failed to create wallet for user");
                        }
                        return userRepository.save(newUser);
                    });
            log.info("User created successfully: {}", user);

            return new UserResponseLastWalletDto(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getDocumentNumber()
            );

        } catch (Exception e) {
            log.error("Failed to create wallet for user: {}", userResquestDto, e);
            throw new RuntimeException("Failed to create wallet for user", e);
        }
    }

    public UserResponseDto getUserByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Invalid email provided");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user == null) {
            throw new RuntimeException("User is null");
        }
        return new UserResponseDto(
                user.getName(),
                user.getEmail(),
                user.getDocumentNumber(),
                user.getWallets()
        );
    }

    public UserResponseDto getUserById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid user ID provided");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user == null) {
            throw new RuntimeException("User is null");
        }
        return new UserResponseDto(
                user.getName(),
                user.getEmail(),
                user.getDocumentNumber(),
                user.getWallets()
        );
    }

    @Transactional
    public UserResponseDto updateUser(Long id, UserResquestDto userResquestDto) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setName(userResquestDto.name());
            user.setEmail(userResquestDto.email());
            user.setDocumentNumber(userResquestDto.document());
            userRepository.save(user);
            return new UserResponseDto(
                    user.getName(),
                    user.getEmail(),
                    user.getDocumentNumber(),
                    user.getWallets()
            );
        } catch (RuntimeException e) {
            log.error("Error updating user: {}", e.getMessage());
            throw new UserException("Error updating user", e);
        }
    }

    public UserResponseDto getUserByDocument(String document) {
        Optional<User> byDocumentNumber = userRepository.findByDocumentNumber(document);
        if (byDocumentNumber.isPresent()) {
            User user = byDocumentNumber.get();
            return new UserResponseDto(
                    user.getName(),
                    user.getEmail(),
                    user.getDocumentNumber(),
                    user.getWallets()
            );
        } else {
            throw new UserException("User not found");
        }
    }
}

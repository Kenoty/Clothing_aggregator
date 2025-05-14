package com.project.clothingaggregator.service;

import com.project.clothingaggregator.cache.MyCache;
import com.project.clothingaggregator.dto.*;
import com.project.clothingaggregator.entity.User;
import com.project.clothingaggregator.exception.NotFoundException;
import com.project.clothingaggregator.mapper.UserMapper;
import com.project.clothingaggregator.repository.UserFavoriteRepository;
import com.project.clothingaggregator.repository.UserRepository;
import com.project.clothingaggregator.security.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.lang.reflect.Field;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserFavoriteRepository userFavoriteRepository;

    @Mock
    private PasswordUtil passwordUtil;

    @Mock
    private MyCache<Integer, UserWithFavoritesDto> cachedBrand;

    @Mock
    private MyCache<Integer, User> cachedUsers;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserRegistrationRequest registrationRequest;
    private UserUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testUser");
        testUser.setEmail("test@example.com");
        testUser.setBirthday(LocalDate.of(1990, 1, 1));
        testUser.setPassword("hashedPassword");

        registrationRequest = new UserRegistrationRequest();
        registrationRequest.setUsername("newUser");
        registrationRequest.setEmail("new@example.com");
        registrationRequest.setBirthday(LocalDate.of(1995, 5, 5));
        registrationRequest.setPassword("password123");

        updateRequest = new UserUpdateRequest();
        updateRequest.setUsername("updatedUser");
        updateRequest.setEmail("updated@example.com");
        updateRequest.setBirthday(LocalDate.of(2000, 1, 1));
    }

    @Test
    void createUser_ValidRequest_ReturnsSavedUser() {
        when(passwordUtil.hashPassword(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.createUser(registrationRequest);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        assertEquals("hashedPassword", result.getPassword());
        verify(userRepository).save(any(User.class));
        verify(passwordUtil).hashPassword("password123");
    }

    @Test
    void updateUser_ExistingId_ReturnsUpdatedUser() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.updateUser(1, updateRequest);

        assertNotNull(result);
        assertEquals("updatedUser", result.getUsername());
        assertEquals("updated@example.com", result.getEmail());
        verify(userRepository).findById(1);
        verify(userRepository).save(testUser);
    }

    @Test
    void updateUser_NonExistingId_ThrowsNotFoundException() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userService.updateUser(99, updateRequest);
        });
        verify(userRepository).findById(99);
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_ExistingId_DeletesUser() throws NoSuchFieldException, IllegalAccessException {
        Field field = UserService.class.getDeclaredField("cachedUsers");
        field.setAccessible(true);
        field.set(userService, cachedUsers);

        when(userRepository.existsById(1)).thenReturn(true);
        when(cachedUsers.get(1)).thenReturn(new User());

        userService.deleteUser(1);

        verify(userRepository).deleteById(1);
        verify(cachedUsers).remove(1);
    }

    @Test
    void deleteUser_NonExistingId_ThrowsNotFoundException() {
        when(userRepository.existsById(99)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> {
            userService.deleteUser(99);
        });
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void getUserById_ExistingUser_ReturnsUser() throws NoSuchFieldException, IllegalAccessException {
        Field field = UserService.class.getDeclaredField("cachedUsers");
        field.setAccessible(true);
        field.set(userService, cachedUsers);

        when(cachedUsers.get(1)).thenReturn(null);
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));

        User result = userService.getUserById(1);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(cachedUsers).put(1, testUser);
    }

    @Test
    void getUserById_CachedUser_ReturnsFromCache() throws NoSuchFieldException, IllegalAccessException {
        Field field = UserService.class.getDeclaredField("cachedUsers");
        field.setAccessible(true);
        field.set(userService, cachedUsers);

        when(cachedUsers.get(1)).thenReturn(testUser);

        User result = userService.getUserById(1);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userRepository, never()).findById(any());
    }

    @Test
    void getUsersByBrand_ShouldReturnEmptyListWhenNoUsersFound() {
        when(userRepository.getAllByFavoriteBrand("Nike")).thenReturn(Collections.emptyList());

        List<UserWithFavoritesDto> result = userService.getUsersByBrand("Nike");

        assertTrue(result.isEmpty());
    }

    @Test
    void getUsersByBrand_ShouldReturnCachedUserWhenAvailable() throws NoSuchFieldException, IllegalAccessException {
        Field field = UserService.class.getDeclaredField("cachedBrand");
        field.setAccessible(true);
        field.set(userService, cachedBrand);

        User user = new User();
        user.setId(1);

        UserWithFavoritesDto cachedDto = new UserWithFavoritesDto();
        cachedDto.setUserId(1);

        when(userRepository.getAllByFavoriteBrand("Adidas")).thenReturn(List.of(user));
        when(cachedBrand.get(1)).thenReturn(cachedDto);

        List<UserWithFavoritesDto> result = userService.getUsersByBrand("Adidas");

        assertEquals(1, result.size());
        assertSame(cachedDto, result.getFirst());
        verify(cachedBrand).get(1);
    }

    @Test
    void getUsersByBrand_ShouldFetchAndCacheWhenUserNotInCache() throws NoSuchFieldException, IllegalAccessException {
        Field field = UserService.class.getDeclaredField("cachedBrand");
        field.setAccessible(true);
        field.set(userService, cachedBrand);

        User user = new User();
        user.setId(1);

        UserWithFavoritesDto newDto = new UserWithFavoritesDto();
        newDto.setUserId(1);

        when(userRepository.getAllByFavoriteBrand("Puma")).thenReturn(List.of(user));
        when(cachedBrand.get(1)).thenReturn(null);
        when(userFavoriteRepository.findAllByUserId(1)).thenReturn(Collections.emptyList());
        when(userMapper.toUserWithFavorites(user)).thenReturn(newDto);

        List<UserWithFavoritesDto> result = userService.getUsersByBrand("Puma");

        assertEquals(1, result.size());
        assertSame(newDto, result.getFirst());
        verify(cachedBrand).get(1);
        verify(userFavoriteRepository).findAllByUserId(1);
        verify(userMapper).toUserWithFavorites(user);
        verify(cachedBrand).put(1, newDto);
    }

}

package org.lab1java.sunsetsunriseapi.controller;

import org.core.sunsetsunrise.controller.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.core.sunsetsunrise.dto.UserDto;
import org.core.sunsetsunrise.entity.Coordinates;
import org.core.sunsetsunrise.entity.User;
import org.core.sunsetsunrise.exception.GlobalExceptionHandler;
import org.core.sunsetsunrise.exception.ResourceNotFoundException;
import org.core.sunsetsunrise.service.RequestCounterService;
import org.core.sunsetsunrise.service.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
  @Mock
  private RequestCounterService requestCounterService;
  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(userController)
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  @Test
  void testGetUserById_Success() {
    User user = new User("Linkong344@gmail.com", "Willygodx", "12345");

    int userId = 1337;

    when(userService.getUserById(userId)).thenReturn(user);

    ResponseEntity<User> response = userController.getUserById(userId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(user, response.getBody());

    Mockito.verify(userService, Mockito.times(1)).getUserById(userId);
    Mockito.verifyNoMoreInteractions(userService);
  }

  @Test
  void testGetUserById_UserNotFound() throws Exception {
    int userId = 228;

    when(userService.getUserById(userId)).thenThrow(
        new ResourceNotFoundException("User not found"));

    mockMvc.perform(MockMvcRequestBuilders.get("/users/get-by-id/{id}", userId))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(404))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found"));

    Mockito.verify(userService, Mockito.times(1)).getUserById(userId);
    Mockito.verifyNoMoreInteractions(userService);
  }

  @Test
  void testGetUserByEmail_Success() {
    User user = new User("Linkong344@gmail.com", "Willygodx", "12345");

    when(userService.getUserByEmail(user.getEmail())).thenReturn(user);

    ResponseEntity<User> response = userController.getUserByEmail(user.getEmail());

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(user, response.getBody());

    Mockito.verify(userService, Mockito.times(1)).getUserByEmail(user.getEmail());
    Mockito.verifyNoMoreInteractions(userService);
  }

  @Test
  void testGetUserByEmail_UserNotFound() throws Exception {
    String email = "Linkong344@gmail.com";

    when(userService.getUserByEmail(email)).thenThrow(
        new ResourceNotFoundException("User not found"));

    mockMvc.perform(MockMvcRequestBuilders.get("/users/get-by-email/{email}", email))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(404))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found"));

    Mockito.verify(userService, Mockito.times(1)).getUserByEmail(email);
    Mockito.verifyNoMoreInteractions(userService);
  }

  @Test
  void testGetUserByNickname_Success() {
    User user = new User("Linkong344@gmail.com", "Willygodx", "12345");

    when(userService.getUserByNickname(user.getNickname())).thenReturn(user);

    ResponseEntity<User> response = userController.getUserByNickname(user.getNickname());

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(user, response.getBody());

    Mockito.verify(userService, Mockito.times(1)).getUserByNickname(user.getNickname());
    Mockito.verifyNoMoreInteractions(userService);
  }

  @Test
  void testGetUserByNickname_UserNotFound() throws Exception {
    String nickname = "Willygodx";

    when(userService.getUserByNickname(nickname)).thenThrow(
        new ResourceNotFoundException("User not found"));

    mockMvc.perform(MockMvcRequestBuilders.get("/users/get-by-nickname/{nickname}", nickname))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(404))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found"));

    Mockito.verify(userService, Mockito.times(1)).getUserByNickname(nickname);
    Mockito.verifyNoMoreInteractions(userService);
  }

  @Test
  void testGetUserCoordinatesList_Success() {
    Page<Coordinates> coordinatesPage = mock(Page.class);

    when(userService.getUserCoordinatesListById(1, 0, 10)).thenReturn(
        coordinatesPage);

    ResponseEntity<Page<Coordinates>> response =
        userController.getUserCoordinatesList(1, 0, 10);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(coordinatesPage, response.getBody());

    Mockito.verify(userService, Mockito.times(1))
        .getUserCoordinatesListById(1, 0, 10);
    Mockito.verifyNoMoreInteractions(userService);
  }

  @Test
  void testGetUserCoordinatesList_UserNotFound() throws Exception {
    int id = 1;
    int pageNumber = 0;
    int pageSize = 10;

    when(userService.getUserCoordinatesListById(1, pageNumber, pageSize))
        .thenThrow(new ResourceNotFoundException("User not found"));

    mockMvc.perform(MockMvcRequestBuilders.get("/users/get-coordinates/{id}", id)
            .param("pageNumber", String.valueOf(pageNumber))
            .param("pageSize", String.valueOf(pageSize)))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(404))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found"));

    Mockito.verify(userService, Mockito.times(1))
        .getUserCoordinatesListById(1, pageNumber, pageSize);
    Mockito.verifyNoMoreInteractions(userService);
  }

  @Test
  void testGetAllUsers() {
    Page<User> userPage = mock(Page.class);

    when(userService.getAllUsers(0, 10)).thenReturn(userPage);

    ResponseEntity<Page<User>> response = userController.getAllUsers(0, 10);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(userPage, response.getBody());

    Mockito.verify(userService, Mockito.times(1)).getAllUsers(0, 10);
    Mockito.verifyNoMoreInteractions(userService);
  }

  @Test
  void testCreateUser() {
    UserDto userDto = new UserDto("Linkong344@gmail.com", "Willygodx", "12345");

    ResponseEntity<String> response = userController.createUser(userDto);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals("Created successfully!", response.getBody());

    Mockito.verify(userService, Mockito.times(1)).createUser(userDto);
    Mockito.verifyNoMoreInteractions(userService);
  }

  @Test
  void testCreateUsersBulk() {
    List<UserDto> userDtoList = new ArrayList<>();
    userDtoList.add(new UserDto("Linkong344@gmail.com", "Willygodx", "12345"));
    userDtoList.add(new UserDto("Matthew9827_us@yahoo.com", "PentestGambler12", "12345"));

    ResponseEntity<String> response = userController.createUsersBulk(userDtoList);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Created successfully!", response.getBody());

    verify(userService, times(1)).createUsersBulk(userDtoList);
    verifyNoMoreInteractions(userService);
  }

  @Test
  void testUpdateUserById() {
    UserDto updateDto = new UserDto("Linkong344@gmail.com", "Willygodx", "12345");

    User updatedUser = new User(updateDto.getEmail(), updateDto.getNickname(), updateDto.getPassword());

    when(userService.updateUserById(22, updateDto)).thenReturn(updatedUser);

    ResponseEntity<User> responseEntity = userController.updateUserById(22, updateDto);

    verify(userService, times(1)).updateUserById(22, updateDto);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(updatedUser, responseEntity.getBody());
  }

  @Test
  void testUpdateUserByEmail() {
    String email = "Linkong344@gmail.com";
    UserDto updateDto = new UserDto("enotland34@yandex.ru", "Sombrero", "12345");

    User updatedUser = new User(updateDto.getEmail(), updateDto.getNickname(), updateDto.getPassword());
    when(userService.updateUserByEmail(email, updateDto)).thenReturn(updatedUser);

    ResponseEntity<User> response = userController.updateUserByEmail(email, updateDto);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(updatedUser, response.getBody());
    verify(userService, times(1)).updateUserByEmail(email, updateDto);
  }

  @Test
  void testUpdateUserByNickname() {
    String nickname = "Willygodx";
    UserDto updateDto = new UserDto("enotland34@yandex.ru", "Sombrero", "12345");

    User updatedUser = new User(updateDto.getEmail(), updateDto.getNickname(), updateDto.getPassword());
    when(userService.updateUserByNickname(nickname, updateDto)).thenReturn(updatedUser);

    ResponseEntity<User> response = userController.updateUserByNickname(nickname, updateDto);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(updatedUser, response.getBody());
    verify(userService, times(1)).updateUserByNickname(nickname, updateDto);
  }

  @Test
  void testDeleteUserById() {
    int id = 123;

    ResponseEntity<String> response = userController.deleteUserById(id);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertEquals("Deleted successfully!", response.getBody());
    verify(userService, times(1)).deleteUserFromDatabaseById(id);
  }
}

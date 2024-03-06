package com.Esliceu.Forum.forum.Controllers;

import com.Esliceu.Forum.forum.DTO.Request.LoginRequestDTO;
import com.Esliceu.Forum.forum.DTO.Request.PasswordRequestDTO;
import com.Esliceu.Forum.forum.DTO.Request.ProfileRequestDTO;
import com.Esliceu.Forum.forum.DTO.Request.RegisterRequestDTO;
import com.Esliceu.Forum.forum.DTO.Response.LoginResponseDTO;
import com.Esliceu.Forum.forum.DTO.Response.ProfileResponseDTO;
import com.Esliceu.Forum.forum.DTO.Response.RegisterResponseDTO;
import com.Esliceu.Forum.forum.Model.Permissions;
import com.Esliceu.Forum.forum.Model.User;
import com.Esliceu.Forum.forum.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    @CrossOrigin

    public RegisterResponseDTO register(@RequestBody RegisterRequestDTO registerRequest, HttpServletResponse resp){
        RegisterResponseDTO response= new RegisterResponseDTO();

        String message =userService.checkUserRegistry(registerRequest);
        if(message.equals("done")){
            response.setMessage(message);
            resp.setStatus(HttpServletResponse.SC_OK);
        }else{
            response.setMessage(message);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return response;
    }

    @PostMapping("/login")
    @CrossOrigin
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse resp){
        String email = loginRequestDTO.getEmail();
        String password = loginRequestDTO.getPassword();
        LoginResponseDTO loginResponseDTO = userService.login(loginRequestDTO.getEmail(),loginRequestDTO.getPassword());
        resp.setStatus(HttpServletResponse.SC_OK);
        return loginResponseDTO;
    }

    @GetMapping("/getprofile")
    @CrossOrigin
    public ProfileResponseDTO getProfile(HttpServletRequest req){
        String token = req.getHeader("Authorization");
        User user = userService.getUserFromToken(token);
        userService.setPermissions(user);
        System.out.println(user.toString());
        ProfileResponseDTO profileResponse;

        profileResponse= userService.castToProfileResponse(user);
        return profileResponse;
    }
    @PutMapping("/profile")
    @CrossOrigin
    public LoginResponseDTO modifyProfile(@RequestBody ProfileRequestDTO profileRequest){
        User user = userService.findUserFromEmail(profileRequest.email());
        user.setName(profileRequest.name());
        userService.modifyUser(user);
        String token = userService.generateToken(user);
        return new LoginResponseDTO(user,token);
    }
    @PutMapping("/profile/password")
    @CrossOrigin
    public boolean changePassword(@RequestBody PasswordRequestDTO passwordRequest, HttpServletRequest request,
    HttpServletResponse response){
        String token = request.getHeader("Authorization");
        User u = userService.getUserFromToken(token);

        String currentPassword = passwordRequest.getCurrentPassword();
        if(userService.verifyPassword(currentPassword,u)){
            String newPasswordEncrypted = userService.encryptPassword(passwordRequest.getNewPassword());
            u.setPassword(newPasswordEncrypted);
            userService.modifyUser(u);

            return true;
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return false;
    }
}

package com.Esliceu.Forum.forum.Services;

import com.Esliceu.Forum.forum.DTO.Request.RegisterRequestDTO;
import com.Esliceu.Forum.forum.DTO.Response.LoginResponseDTO;
import com.Esliceu.Forum.forum.DTO.Response.ProfileResponseDTO;
import com.Esliceu.Forum.forum.Model.Permissions;
import com.Esliceu.Forum.forum.Model.User;
import com.Esliceu.Forum.forum.Repositories.CategoriesRepo;
import com.Esliceu.Forum.forum.Repositories.UserRepo;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Date;
import java.util.List;


@Service
public class UserService {
    @Value("${token.expiration.time}")
    int expiration;
    @Value("${token.secret")
    String tokenSecret;
    private static final int minPasswordLength=8;
    @Autowired
    UserRepo userRepo;
    @Autowired
    CategoriesService categoriesService;
    public String checkUserRegistry(RegisterRequestDTO registerRequest){
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();
        Permissions permissions = new Permissions();
        if(userRepo.findByemail(email).isPresent()){
            return "The user already exists";
        }
        if (password.length()<minPasswordLength) {
            return "The password is too short";
        }else if(!checkPassword(password)){
            return "The password must contain a number, a capital letter and a special character";
        }
        String encryptedPassword = encryptPassword(password);
        User user = new User();
        user.setName(registerRequest.getName());
        user.setRole(registerRequest.getRole());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encryptedPassword);
        permissions.setRoot(registerRequest.getRole());

        userRepo.save(user);
        return "done";
    }
    public boolean verifyPassword(String password, User user) {
        String encryptedPassword = encryptPassword(password);
        if (user.getPassword().equals(encryptedPassword)){
            return true;
        }else{
            return false;
        }
    }
    public void modifyUser(User user){
        userRepo.save(user);
    }
    private boolean checkPassword(String password){
        String regex =  "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$";

        boolean aux = password.matches(regex);

        return aux;
    }
    public String encryptPassword(String password) {

            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(password.getBytes());

                // Convert the byte array to a hexadecimal string
                StringBuilder hexString = new StringBuilder();
                for (byte b : hash) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) {
                        hexString.append('0');
                    }
                    hexString.append(hex);
                }


                return hexString.toString();
            } catch (NoSuchAlgorithmException e) {
                // Handle the exception or rethrow as needed
                throw new RuntimeException("Error encrypting password", e);

            }

    }

    public LoginResponseDTO login(String email, String password) {
        String token = "";
        User u = null;

        String encryptedPassword = encryptPassword(password);

        if (userRepo.findByemail(email).isPresent()) {
            u = userRepo.findByemail(email).get();
            if (u.getPassword().equals(encryptedPassword)) {
                setPermissions(u);
                token = generateToken(u);
            }
        }
        return new LoginResponseDTO(u,token);
    }

    public User getUserFromToken(String token){
        String tokenString = token.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(tokenSecret))
                .build().verify(tokenString);
        String email = decodedJWT.getClaim("email").asString();
        return userRepo.findByemail(email).get();
    }

    public User findUserFromEmail(String email) {
        return userRepo.findByemail(email).get();
    }
    public String generateToken(User u){
        return JWT.create()
                .withClaim("role", u.getRole())
                .withClaim("_id", u.getUserId())
                .withClaim("email", u.getEmail())
                .withClaim("name", u.getName())
                .withClaim("__v", 0)
                .withClaim("avatarUrl", u.getAvatar())
                .withClaim("id", u.getUserId())
                .withClaim("permissions",u.getPermissions().getRoot())
                .withIssuedAt(Instant.now())
                .withExpiresAt(new Date((System.currentTimeMillis() + expiration)))
                .sign(Algorithm.HMAC512(tokenSecret));
    }

    public ProfileResponseDTO castToProfileResponse(User user) {
        return new ProfileResponseDTO(user.getRole(),user.getUserId(),user.getEmail(),user.getName(),user.getAvatar(),
                user.getUserId(),user.getPermissions());
    }

    public void setPermissions(User u){
        Permissions permissions = new Permissions();
        permissions.setRoot(u.getRole());
        permissions.setCategories(categoriesService.retrieveCategoriesNames(),u.getRole());
        u.setPermissions(permissions);

    }
}

package com.Esliceu.Forum.forum.DTO.Response;

import com.Esliceu.Forum.forum.Model.User;

public record LoginResponseDTO (User user, String token) {


}

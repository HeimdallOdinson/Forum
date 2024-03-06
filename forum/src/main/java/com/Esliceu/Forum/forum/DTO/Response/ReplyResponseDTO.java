package com.Esliceu.Forum.forum.DTO.Response;

import java.util.Date;
import com.Esliceu.Forum.forum.Model.Topic;
import com.Esliceu.Forum.forum.Model.User;



public record ReplyResponseDTO(Long _id, String content, Long topic,
                               User user, Date createdAt, Date updatedAt,
                               int __v){
}

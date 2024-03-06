package com.Esliceu.Forum.forum.DTO.Response;

import com.Esliceu.Forum.forum.DTO.ReplyDTO;
import com.Esliceu.Forum.forum.DTO.UserDTO;
import com.Esliceu.Forum.forum.Model.Category;
import com.Esliceu.Forum.forum.Model.Reply;
import com.Esliceu.Forum.forum.Model.User;

import java.util.Date;

public record TopicResponseDTO (int views, Long _id, String title,
                                String content, Long category, User user,
                                Date createdAt, Date updatedAt, int __v,
                                Reply[] replies, int numberOfReplies, Long id){
}

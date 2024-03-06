package com.Esliceu.Forum.forum.Controllers;

import com.Esliceu.Forum.forum.DTO.Request.ReplyRequestDTO;
import com.Esliceu.Forum.forum.DTO.Response.ReplyResponseDTO;
import com.Esliceu.Forum.forum.DTO.Response.TopicResponseDTO;
import com.Esliceu.Forum.forum.Exceptions.ActionNotAllowedException;
import com.Esliceu.Forum.forum.Model.Permissions;
import com.Esliceu.Forum.forum.Model.Reply;
import com.Esliceu.Forum.forum.Model.Topic;
import com.Esliceu.Forum.forum.Model.User;
import com.Esliceu.Forum.forum.Services.ReplyService;
import com.Esliceu.Forum.forum.Services.TopicService;
import com.Esliceu.Forum.forum.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RepliesController {
    @Autowired
    ReplyService replyService;
    @Autowired
    TopicService topicService;
    @Autowired
    UserService userService;
    @GetMapping("/topics/{topicID}")
    @CrossOrigin
    public TopicResponseDTO getRepliesFromTopic(@PathVariable Long topicID){

        Topic topic = topicService.findTopicByID(topicID);
        User user = topic.getUser();
        TopicResponseDTO responseDTO = topicService.castToDTO(topic,user);
        return responseDTO;
    }
    @PostMapping("/topics/{topicId}/replies")
    @CrossOrigin
    public ReplyResponseDTO createReply(@PathVariable Long topicId, @RequestBody ReplyRequestDTO replyRequest,
                                        HttpServletRequest request, HttpServletResponse response){
        String token = request.getHeader("Authorization");
        User user = userService.getUserFromToken(token);
        Permissions permissions= new Permissions();
        permissions.setRoot(user.getRole());
        System.out.println("Estamos creando una reply");

        System.out.println("El usuario es "+user.toString());
        Reply reply =replyService.createReply(topicId,replyRequest.getContent(),user);
        if(reply.getContent().equals(replyRequest.getContent())){
            return new ReplyResponseDTO(reply.get_id(),reply.getContent(),reply.getTopic().getTopicID(),
                    reply.getUser(),reply.getCreatedAt(),reply.getUpdatedAt(),0);
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return null;
    }

    @DeleteMapping("/topics/{topicId}/replies/{replyID}")
    @CrossOrigin
    public boolean deleteReply(@PathVariable Long topicId, @PathVariable Long replyID, HttpServletResponse response,
                               HttpServletRequest request){
        String token = request.getHeader("Authorization");
        User user = userService.getUserFromToken(token);
        Reply reply = replyService.getReplyFromID(replyID);
        if(user.getRole().equals("user")&& !reply.getUser().equals(user)){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            throw new ActionNotAllowedException("You are not allowed to delete this reply");
        }
        replyService.deleteReply(replyID);
        return true;
    }
    @PutMapping("/topics/{topicId}/replies/{replyID}")
    @CrossOrigin
    public boolean modifyReply(@PathVariable Long topicId, @PathVariable Long replyID, ReplyRequestDTO replyRequest,
                               HttpServletRequest request, HttpServletResponse response){
        String token = request.getHeader("Authorization");
        User user = userService.getUserFromToken(token);
        Reply reply = replyService.getReplyFromID(replyID);
        if(user.getRole().equals("user")&& !reply.getUser().equals(user)){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            throw new ActionNotAllowedException("You are not allowed to modify this reply");
        }
        String content = replyRequest.getContent();
        replyService.modifyReply(replyID, content);
        return true;
    }
}

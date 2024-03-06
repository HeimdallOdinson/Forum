package com.Esliceu.Forum.forum.Services;

import com.Esliceu.Forum.forum.Model.User;
import com.Esliceu.Forum.forum.Model.Reply;
import org.springframework.stereotype.Service;
import com.Esliceu.Forum.forum.Repositories.ReplyRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLOutput;
import java.util.List;


@Service
public class ReplyService {
    @Autowired
    TopicService topicService;
    @Autowired
    ReplyRepo replyRepo;
    public Reply createReply(Long topicId, String content, User user) {
        System.out.println("El usuario es "+user.toString());
        Reply reply = new Reply();
        reply.setUser(user);
        reply.setContent(content);
        reply.setTopic(topicService.findTopicByID(topicId));
        Reply replyCreated=replyRepo.save(reply);
        return replyCreated;
    }



    public void deleteReply(Long replyID) {
        Reply reply = replyRepo.findById(replyID).get();
        replyRepo.delete(reply);
    }

    public void modifyReply(Long replyID, String content) {
        Reply reply =replyRepo.findById(replyID).get();
        reply.setContent(content);
        replyRepo.save(reply);
    }

    public Reply getReplyFromID(Long replyID){
        return replyRepo.findById(replyID).get();
    }
}

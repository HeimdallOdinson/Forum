package com.Esliceu.Forum.forum.Services;

import com.Esliceu.Forum.forum.DTO.ReplyDTO;
import com.Esliceu.Forum.forum.DTO.Response.TopicResponseDTO;
import com.Esliceu.Forum.forum.Model.Category;
import com.Esliceu.Forum.forum.Model.Reply;
import com.Esliceu.Forum.forum.Model.Topic;
import com.Esliceu.Forum.forum.Model.User;
import com.Esliceu.Forum.forum.Repositories.TopicRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TopicService {
    @Autowired
    TopicRepo topicRepo;
    @Autowired
    UserService userService;

    public List<TopicResponseDTO> findAllTopicsFromCategory (String categorySlug, String token){


        return topicRepo.findByCategory_Slug(categorySlug).stream().
                map(topic -> new TopicResponseDTO(topic.getViews(),topic.getTopicID(),topic.getTitle(),
                        topic.getContent(),topic.getCategory().getCategoryID(),
                        topic.getUser(), topic.getCreatedAt(),topic.getUpdatedAt(), 0, null,topic.getNumberOfReplies(),
                        topic.getTopicID())).collect(Collectors.toList());
    }

    public Topic createTopic(Category category, String title, String content, String token) {

        User user = userService.getUserFromToken(token);
        Topic newTopic = new Topic();
        newTopic.setTitle(title);
        newTopic.setContent(content);
        newTopic.setCategory(category);
        newTopic.setUser(user);
        Topic createdTopic = topicRepo.save(newTopic);

        return createdTopic;
    }
    public void modifyTopic(Topic topic){
        topicRepo.save(topic);
    }

    public TopicResponseDTO castToDTO(Topic topic, User u) {
        //Set<ReplyDTO> repliesSet = castReplyToReplyDTO(topic.getReplies());
        Set<Reply> repliesSet = topic.getReplies();
        Reply[] repliesArray = (repliesSet != null) ? repliesSet.toArray(new Reply[0]) : new Reply[0];
        
        return new TopicResponseDTO(topic.getViews(), topic.getTopicID(),
                topic.getTitle(),topic.getContent(),topic.getCategory().getCategoryID(),
                u,topic.getCreatedAt(),topic.getUpdatedAt(),0,repliesArray,0, topic.getTopicID());
    }

    public Topic findTopicByID(Long topicID) {
        Topic topic = topicRepo.findById(topicID).get();
        return topic;
    }

    public void deleteTopic(Topic topic) {
        topicRepo.delete(topic);
    }
    private Set<ReplyDTO> castReplyToReplyDTO(Set<Reply> replySet) {
        return replySet.stream()
                .map(reply -> new ReplyDTO(
                        reply.get_id(),
                        reply.getContent(),
                        reply.getTopic().getTopicID()
                ))
                .collect(Collectors.toSet());
    }
}

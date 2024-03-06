package com.Esliceu.Forum.forum.Repositories;

import com.Esliceu.Forum.forum.Model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepo extends JpaRepository<Reply, Long> {


   List<Reply> findByTopic_topicID(Long topicId);
}

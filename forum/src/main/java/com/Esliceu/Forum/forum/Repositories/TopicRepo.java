package com.Esliceu.Forum.forum.Repositories;

import com.Esliceu.Forum.forum.Model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepo extends JpaRepository<Topic, Long> {
    public List<Topic> findByCategory_Slug(String slug);
}

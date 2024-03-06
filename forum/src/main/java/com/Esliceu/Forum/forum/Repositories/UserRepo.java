package com.Esliceu.Forum.forum.Repositories;

import com.Esliceu.Forum.forum.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    public Optional<User> findByemail(String email);
}

package com.Esliceu.Forum.forum.Model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="reply_id")
    Long _id;

    @ManyToOne
    @JoinColumn(name="topicID", nullable = false)
    Topic topic;

    String content;

    @ManyToOne
    @JoinColumn(name="userId")
    User user;

    @Column(name ="created_at")
    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt;

    @Column(name ="updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    Date updatedAt;
    @Transient
    private int __v=0;
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }


    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }
}

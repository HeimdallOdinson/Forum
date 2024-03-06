package com.Esliceu.Forum.forum.DTO.Response;

import com.Esliceu.Forum.forum.Model.Permissions;

public class ProfileResponseDTO {

    private String role;
    private Long _id;
    private String email;
    private String name;
    private final int __v=0;
    private String avatarUrl;
    private Long id;

    private Permissions permissions;

    public ProfileResponseDTO(String role, Long _id, String email, String name, String avatarUrl, Long id, Permissions permissions) {
        this.role = role;
        this._id = _id;
        this.email = email;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.id = id;
        this.permissions = permissions;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int get__v() {
        return __v;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }
}

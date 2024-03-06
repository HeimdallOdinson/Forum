package com.Esliceu.Forum.forum.Model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Permissions {

    private List<String> root;
    private Map<String, List<String>> categories = new HashMap<>();


    public List<String> getRoot() {
        return root;
    }

    public void setRoot(String role) {
        switch(role){
            case "admin":
                this.root = Arrays.asList(
                        "own_topics:write",
                        "own_topics:delete",
                        "own_replies:write",
                        "own_replies:delete",
                        "categories:write",
                        "categories:delete"
                );
                break;
            case "Moderator":
                break;
            case "user":
                this.root = Arrays.asList(
                        "own_topics:write",
                        "own_topics:delete",
                        "own_replies:write",
                        "own_replies:delete"
                );
                break;
        }
    }

    public Map<String, List<String>> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories, String role) {
        switch (role){
            case "admin":

                List<String> values= Arrays.asList(
                        "categories_topics:write",
                        "categories_topics:delete",
                        "categories_replies:write",
                        "categories_replies:delete"
                );
                for (int i = 0; i < categories.size(); i++) {
                    this.categories.put(categories.get(i), values);
                }
                break;
            case "moderator":
                break;
            case "user":

                break;
            }
        }


}

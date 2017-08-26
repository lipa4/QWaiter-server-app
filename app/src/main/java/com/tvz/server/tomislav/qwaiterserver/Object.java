package com.tvz.server.tomislav.qwaiterserver;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by tomislav on 26/08/17.
 */

public class Object {

    private String name;
    private String category;
    private String avatarImage;
    private String backgroundImage;
    private DatabaseReference reference;

    public Object() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(String avatarImage) {
        this.avatarImage = avatarImage;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public DatabaseReference getReference() {
        return reference;
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }
}

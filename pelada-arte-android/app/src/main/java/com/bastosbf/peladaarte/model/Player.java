package com.bastosbf.peladaarte.model;

import java.io.Serializable;

/**
 * Created by bastosbf on 17/05/16.
 */
public class Player implements Serializable {

    private String email;
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if(o instanceof  Player) {
            return ((Player) o).email.equals(email);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}

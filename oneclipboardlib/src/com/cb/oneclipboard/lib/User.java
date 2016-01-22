package com.cb.oneclipboard.lib;

import java.io.Serializable;

public class User implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6709827468159234065L;
    private String userName;
    private String sha256Hash;

    public User(String userName, String sha256Hash) {
        super();
        this.userName = userName;
        this.sha256Hash = sha256Hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return sha256Hash != null ? sha256Hash.equals(user.sha256Hash) : user.sha256Hash == null;

    }

    @Override
    public int hashCode() {
        return sha256Hash != null ? sha256Hash.hashCode() : 0;
    }

    public String getSha256Hash() {
        return sha256Hash;
    }

    public void setSha256Hash(String sha256Hash) {
        this.sha256Hash = sha256Hash;
    }

    public String getUserName() {
        return userName;
    }
}

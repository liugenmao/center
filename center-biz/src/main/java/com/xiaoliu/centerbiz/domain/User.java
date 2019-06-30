package com.xiaoliu.centerbiz.domain;

import java.util.Date;

/**
 * @author
 * @date
 **/
public class User {

    /**
     *
     **/
    private Long id;
    /**
     *
     **/
    private String username;
    /**
     *
     **/
    private String password;
    /**
     *
     **/
    private Boolean isEnabled;
    /**
     *
     **/
    private String realname;
    /**
     *
     **/
    private String phoneNumber;
    /**
     *
     **/
    private Date created;
    /**
     *
     **/
    private Date modified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }
}
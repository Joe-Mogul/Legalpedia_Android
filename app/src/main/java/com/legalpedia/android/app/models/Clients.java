package com.legalpedia.android.app.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by adebayoolabode on 12/22/16.
 */
@Entity( createInDb = false)
public class Clients {
    @Id
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String facetime;
    private String skype;
    @Generated(hash = 1607671708)
    public Clients(Long id, String name, String phone, String email, String address,
            String facetime, String skype) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.facetime = facetime;
        this.skype = skype;
    }

    @Generated(hash = 1891515851)
    public Clients() {
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFacetime() {
        return facetime;
    }

    public void setFacetime(String facetime) {
        this.facetime = facetime;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }



}

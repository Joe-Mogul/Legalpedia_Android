package com.legalpedia.android.app.models;

/**
 * Created by adebayoolabode on 11/7/16.
 */
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;



@Entity( createInDb = false)
public class Profile {
    @Id
    private Long id;
    private int uid ;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String biography;
    private String profilepics;
    private String skype;
    private String facetime;
    private String address1;
    private String address2;
    private String city;
    private String city_other;
    private String state;
    private String state_other;
    private String town;
    private String town_other;
    private String country;
    private Date createdate;

    @Generated(hash = 538455983)
    public Profile(Long id, int uid, String firstname, String lastname,
            String email, String phone, String biography, String profilepics,
            String skype, String facetime, String address1, String address2,
            String city, String city_other, String state, String state_other,
            String town, String town_other, String country, Date createdate) {
        this.id = id;
        this.uid = uid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.biography = biography;
        this.profilepics = profilepics;
        this.skype = skype;
        this.facetime = facetime;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.city_other = city_other;
        this.state = state;
        this.state_other = state_other;
        this.town = town;
        this.town_other = town_other;
        this.country = country;
        this.createdate = createdate;
    }
    @Generated(hash = 782787822)
    public Profile() {
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getUid() {
        return this.uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }
    public String getFirstname() {
        return this.firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public String getLastname() {
        return this.lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getProfilepics() {
        return profilepics;
    }

    public void setProfilepics(String profilepics) {
        this.profilepics = profilepics;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }


    public String getSkype() {
        return this.skype;
    }
    public void setSkype(String skype) {
        this.skype = skype;
    }
    public String getFacetime() {
        return this.facetime;
    }
    public void setFacetime(String facetime) {
        this.facetime = facetime;
    }
    public String getAddress1() {
        return this.address1;
    }
    public void setAddress1(String address1) {
        this.address1 = address1;
    }
    public String getAddress2() {
        return this.address2;
    }
    public void setAddress2(String address2) {
        this.address2 = address2;
    }
    public String getCity() {
        return this.city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCity_other() {
        return this.city_other;
    }
    public void setCity_other(String city_other) {
        this.city_other = city_other;
    }
    public String getState() {
        return this.state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getState_other() {
        return this.state_other;
    }
    public void setState_other(String state_other) {
        this.state_other = state_other;
    }
    public String getTown() {
        return this.town;
    }
    public void setTown(String town) {
        this.town = town;
    }
    public String getTown_other() {
        return this.town_other;
    }
    public void setTown_other(String town_other) {
        this.town_other = town_other;
    }
    public String getCountry() {
        return this.country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public Date getCreatedate() {
        return this.createdate;
    }
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

}

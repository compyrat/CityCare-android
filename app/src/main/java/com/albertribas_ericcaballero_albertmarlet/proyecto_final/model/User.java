package com.albertribas_ericcaballero_albertmarlet.proyecto_final.model;

/**
 * Created by albertmarnun on 26/04/2016.
 */
public class User {
    private String _id = "";
    private String _name = "";
    private String _email = "";
    private String _photoUrl = "";
    private String _gender = "";
    private String _birthday = "";
    private String _lastName = "";
    private String _password = "";
    private int _possitivePoints = 0;
    private int _negativePoints = 0;

    public User(){

    }
    public User(String id, String name, String email, String photoUrl, String gender, String birthday){
        setId(id);
        setName(name);
        setEmail(email);
        setPhotoUrl(photoUrl);
        setGender(gender);
        setBirthday(birthday);
        setLastName("");
        setPassword("");
    }
    public User(String id, String name, String email, String photoUrl, String gender, String birthday, String lasName, String pass){
        setId(id);
        setName(name);
        setEmail(email);
        setPhotoUrl(photoUrl);
        setGender(gender);
        setBirthday(birthday);
        setLastName(lasName);
        setPassword(pass);
    }

    public User(String id, String name, String email, String photoUrl, String gender, String birthday, String lasName, String pass, int possiteve, int negative){
        setId(id);
        setName(name);
        setEmail(email);
        setPhotoUrl(photoUrl);
        setGender(gender);
        setBirthday(birthday);
        setLastName(lasName);
        setPassword(pass);
        setNegativePoints(negative);
        setPossitivePoints(possiteve);
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public String getEmail() {
        return _email;
    }

    public void setEmail(String _email) {
        this._email = _email;
    }

    public String getPhotoUrl() {
        return _photoUrl;
    }

    public void setPhotoUrl(String _photoUrl) {
        this._photoUrl = _photoUrl;
    }

    public String getGender() {
        return _gender;
    }

    public void setGender(String _gender) {
        this._gender = _gender;
    }

    public String getBirthday() {
        return _birthday;
    }

    public void setBirthday(String _birthday) {
        this._birthday = _birthday;
    }

    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String _lastName) {
        this._lastName = _lastName;
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(String _password) {
        this._password = _password;
    }

    public int getPossitivePoints() {
        return _possitivePoints;
    }

    public void setPossitivePoints(int _possitivePoints) {
        this._possitivePoints = _possitivePoints;
    }

    public int getNegativePoints() {
        return _negativePoints;
    }

    public void setNegativePoints(int _negativePoints) {
        this._negativePoints = _negativePoints;
    }
}

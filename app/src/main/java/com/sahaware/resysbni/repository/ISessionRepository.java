package com.sahaware.resysbni.repository;


import java.util.HashMap;

public interface ISessionRepository {
    void createIPSession(String ip);
    String getIP();
    String getId();
    String getToken();
    String getRules();
    String getUsername();
    void createLoginSession(String id, String username, String token, String rules);
    void checkLogin();
    void logoutUser();
    HashMap<String, String> getUserDetails();
    boolean isLoggedIn();
}
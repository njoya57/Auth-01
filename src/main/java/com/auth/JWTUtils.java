/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.auth;

/**
 *
 * @author riyaro
 */
public class JWTUtils {

    public static final String SECRET_KEY = "My secret key";
    public static final String AUTH_HEADER = "Authorization";
    public static final String FREFIX = "Bearer ";
    public static final long EXPIRE_REFRESH_TOKEN = 5 * 60 * 1000;
    public static final long EXPIRE_ACCESS_TOKEN = 1 * 60 * 1000;
}

package com.ragku.rest.web;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class ClassLoadTest {
	public static void main(String[] args) {
		c();
	}
	
	public static void a() {
		try {
			Enumeration<URL>  list = Thread.currentThread().getContextClassLoader().getResources("?");
			while(list.hasMoreElements()) {
				System.out.println(list.nextElement().getPath());;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void b() {
		try {
			Enumeration<URL>  list = ClassLoader.getSystemClassLoader().getResources("");
			while(list.hasMoreElements()) {
				System.out.println(list.nextElement().getPath());;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void c() {
	}
}

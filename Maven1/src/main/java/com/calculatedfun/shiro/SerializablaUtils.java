package com.calculatedfun.shiro;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.Session;

public class SerializablaUtils {
	public static String serialize(Session session) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(session);
			return Base64.encodeToString(bos.toByteArray());
		} catch (Exception e) {
			throw new RuntimeException("serialize session error", e);
		}
	}

	public static Session deserialize(String sessionStr) {
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decode(sessionStr));
			ObjectInputStream ois = new ObjectInputStream(bis);
			return (Session) ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException("deserialize session error", e);
		}
	}

}

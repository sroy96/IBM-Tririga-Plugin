package com.tririga.custom.workflow.util;

import java.io.File;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

public class NotificationUtil {

	private static final Logger log = Logger.getLogger(NotificationUtil.class);

	public static File getFileFromResources(final String fileName) {
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();

		URL resource = classLoader.getResource(fileName);
		if (null == resource) {
			throw new IllegalArgumentException("Firebase service json file not found");
		} else {
			return new File(resource.getFile());
		}
	}
	
	public static String getRegistionToken(final String userId, final String collectionName, final Firestore db) {
		try {
			if (null != db) {
				DocumentReference document = db.collection(collectionName).document(userId);
				ApiFuture<DocumentSnapshot> apiFuture = document.get();
				DocumentSnapshot documentSnapshot = apiFuture.get();
				if (null != documentSnapshot && documentSnapshot.exists()) {
					return (String) documentSnapshot.get("fb_pn_access_token");
				}
			}
		} catch (InterruptedException e) {
			log.error("Error while fetching the registration token", e);
		} catch (ExecutionException e) {
			log.error("Execution exception while fetching the registration token", e);
		}
		return null;
	}
	
	public static String sendNotification(final Message message) {
		try {
			return FirebaseMessaging.getInstance().send(message);
		} catch (FirebaseMessagingException e) {
			log.error("Error while sending the notification", e);
		}
		
		return null;
	}
}

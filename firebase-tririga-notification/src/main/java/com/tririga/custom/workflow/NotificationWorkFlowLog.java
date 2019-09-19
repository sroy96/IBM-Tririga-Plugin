package com.tririga.custom.workflow;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.Message;
import com.tririga.custom.workflow.util.ApplicationProperties;
import com.tririga.custom.workflow.util.NotificationUtil;

public class NotificationWorkFlowLog {

	private static final Logger log = Logger.getLogger(NotificationWorkFlowLog.class);
	private static String USER_ID = "testResource1";

	public Firestore getFirestoreDatabase() {
		try {
			InputStream inputStream = new FileInputStream(
					NotificationUtil.getFileFromResources(ApplicationProperties.INSTANCE.getfirebaseAccountName()));
			GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
			FirebaseOptions firebaseOptions = new FirebaseOptions.Builder().setCredentials(credentials).build();
			FirebaseApp.initializeApp(firebaseOptions);
			return FirestoreClient.getFirestore();
		} catch (IOException e) {
			log.error("Error while initializing the filestore database ", e);
		}
		return null;
	}

	public static void main(String[] args) {
		sendPushNotification(USER_ID, "OCiFM Tririga", "WorkTask been assigned to Linda", "173773", "99485577");
	}

	private static void sendPushNotification(final String userId, final String title, final String message, final String incidentId,
			final String workTaskId) {
		NotificationWorkFlowLog nwfl = new NotificationWorkFlowLog();
		Firestore db = nwfl.getFirestoreDatabase();

		String registrationToken = NotificationUtil.getRegistionToken(userId,
				ApplicationProperties.INSTANCE.getfirestoreCollectionName(), db);
		System.out.println("Registraion token: " + registrationToken);

		Message messageNotif = Message.builder().putData("title", title).putData("message", message)
				.putData("incidentId", incidentId).putData("workTaskId", workTaskId).setToken(registrationToken)
				.build();

		String response = NotificationUtil.sendNotification(messageNotif);

		if (null != response)
			log.info("Successfully sent message: " + response);
		System.out.println("Successfully sent message: " + response);
	}
}

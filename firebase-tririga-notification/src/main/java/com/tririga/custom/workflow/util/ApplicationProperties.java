package com.tririga.custom.workflow.util;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public enum ApplicationProperties {
	INSTANCE;

    private final Properties properties;

    ApplicationProperties() {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public String getfirebaseAccountName() {
        return properties.getProperty("firebase.service.account.file.name");
    }
    
    public String getfirestoreCollectionName() {
        return properties.getProperty("collection.name");
    }
}

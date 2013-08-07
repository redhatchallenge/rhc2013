package org.ayrx.rhchallenge.client;

import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageMap;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public enum LocalStorage {

    INSTANCE;

    private Storage localStorage = Storage.getLocalStorageIfSupported();

    public Storage getLocalStorage() {
        return localStorage;
    }

    public void clearLocalStorage() {
        localStorage.clear();
    }
}
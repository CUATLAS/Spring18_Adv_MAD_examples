package com.example.aileen.booklist;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BookListApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        //initialize Realm
        Realm.init(this);
        //define the configuration for realm
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        //set the default realm configuration
        Realm.setDefaultConfiguration(realmConfig);
    }
}

package ru.geekbrains.android3_7;

import io.paperdb.Paper;
import io.realm.Realm;
import ru.geekbrains.android3_7.di.AppComponent;
import ru.geekbrains.android3_7.di.DaggerAppComponent;

public class App extends com.activeandroid.app.Application
{
    private static App instance;

    private AppComponent appComponent;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        Paper.init(this);

        Realm.init(this);

        appComponent = DaggerAppComponent.builder().build();

    }

    public static App getInstance()
    {
        return instance;
    }

    public AppComponent getAppComponent()
    {
        return appComponent;
    }

}

package ru.geekbrains.android3_7.model.api;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ru.geekbrains.android3_7.NetworkStatus;
import ru.geekbrains.android3_7.model.cache.ICache;
import ru.geekbrains.android3_7.model.entity.User;
import ru.geekbrains.android3_7.model.entity.UserRepository;

public class UserRepo implements IUserRepo
{
    private static final String TAG = "ActiveAndroidUserRepo";

    ICache cache;
    ApiService api;

    public UserRepo(ApiService api, ICache cache)
    {
        this.cache = cache;
        this.api = api;
    }

    public Observable<User> getUser(String username)
    {
        if (NetworkStatus.isOnline())
        {
            Observable<User> observable = api.getUser(username).subscribeOn(Schedulers.io());
            observable.subscribe(user -> cache.putUser(user));
            return observable;
        }
        else
        {
           return cache.getUser(username);
        }
    }

    public Observable<List<UserRepository>> getUserRepos(User user)
    {
        if (NetworkStatus.isOnline())
        {
            Observable<List<UserRepository>> observable = api.getUserRepos(user.getLogin()).subscribeOn(Schedulers.io());
            observable.subscribe(repos -> cache.putUserRepositories(user, repos));
            return observable;
        }
        else
        {
            return cache.getUserRepositories(user);
        }
    }
}

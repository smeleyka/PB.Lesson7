package ru.geekbrains.android3_7.model.api;

import java.util.List;

import io.paperdb.Paper;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ru.geekbrains.android3_7.NetworkStatus;
import ru.geekbrains.android3_7.model.entity.User;
import ru.geekbrains.android3_7.model.entity.UserRepository;

/**
 * Created by stanislav on 3/12/2018.
 */

public class PaperUserRepo implements IUserRepo
{
    ApiService api;

    public Observable<User> getUser(String username)
    {
        if(NetworkStatus.isOnline())
        {
            Observable<User> observable = api.getUser(username).subscribeOn(Schedulers.io());
            observable.subscribe(user -> Paper.book("data").write("user", user));
            return observable;
        }
        else
        {
            return Observable.create(e -> {

                if(Paper.book("data").contains("user"))
                {
                    e.onNext(Paper.book("data").read("user"));
                }
                else
                {
                    e.onError(new RuntimeException("No user in cache"));
                }
                e.onComplete();
            });
        }
    }

    public Observable<List<UserRepository>> getUserRepos(User user)
    {

        if(NetworkStatus.isOnline())
        {
            Observable<List<UserRepository>> observable = api.getUserRepos(user.getLogin()).subscribeOn(Schedulers.io());
            observable.subscribe(repos -> Paper.book("data").write("repos", repos));
            return observable;
        }
        else
        {
            return Observable.create(e -> {

                if(Paper.book("data").contains("repos"))
                {
                    e.onNext(Paper.book("data").read("repos"));
                }
                else
                {
                    e.onError(new RuntimeException("No user in cache"));
                }
                e.onComplete();
            });
        }
    }
}

package ru.geekbrains.android3_7.model.cache;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import ru.geekbrains.android3_7.model.entity.User;
import ru.geekbrains.android3_7.model.entity.UserRepository;
import ru.geekbrains.android3_7.model.entity.activeandroid.AAUser;
import ru.geekbrains.android3_7.model.entity.activeandroid.AAUserRepository;

public class AACache implements ICache
{
    @Override
    public void putUser(User user)
    {
        AAUser aaUser = new Select()
                .from(AAUser.class)
                .where("login = ?", user.getLogin())
                .executeSingle();

        if (aaUser == null)
        {
            aaUser = new AAUser();
            aaUser.login = user.getLogin();
        }

        aaUser.avatarUrl = user.getAvatarUrl();
        aaUser.save();
    }

    @Override
    public Observable<User> getUser(String username)
    {
        return Observable.create(e ->
        {
            AAUser aaUser = new Select()
                    .from(AAUser.class)
                    .where("login = ?", username)
                    .executeSingle();


            if (aaUser == null)
            {
                e.onError(new RuntimeException("No user in cache"));
            } else
            {
                e.onNext(new User(aaUser.login, aaUser.avatarUrl));
            }
            e.onComplete();
        });
    }

    @Override
    public void putUserRepositories(User user, List<UserRepository> repositories)
    {

        AAUser aaUser = new Select()
                .from(AAUser.class)
                .where("login = ?", user.getLogin())
                .executeSingle();
        if (aaUser == null)
        {
            aaUser = new AAUser();
            aaUser.login = user.getLogin();
            aaUser.avatarUrl = user.getAvatarUrl();
            aaUser.save();
        }

        new Delete().from(AAUserRepository.class).where("user = ?", aaUser);

        ActiveAndroid.beginTransaction();
        try
        {
            for (UserRepository repo : repositories)
            {
                AAUserRepository aaUserRepository = new AAUserRepository();
                aaUserRepository.id = repo.getId();
                aaUserRepository.name = repo.getName();
                aaUserRepository.user = aaUser;
                aaUserRepository.save();
            }

            ActiveAndroid.setTransactionSuccessful();
        } finally
        {
            ActiveAndroid.endTransaction();
        }
    }

    @Override
    public Observable<List<UserRepository>> getUserRepositories(User user)
    {
        return Observable.create(e ->
        {

            AAUser aaUser = new Select()
                    .from(AAUser.class)
                    .where("login = ?", user.getLogin())
                    .executeSingle();

            if (aaUser == null)
            {
                e.onError(new RuntimeException("No user in cache"));
            }
            else
            {
                List<UserRepository> repos = new ArrayList<>();
                for (AAUserRepository aaUserRepository : aaUser.repositories())
                {
                    repos.add(new UserRepository(aaUserRepository.id, aaUserRepository.name));
                }
                e.onNext(repos);
            }
            e.onComplete();
        });
    }
}

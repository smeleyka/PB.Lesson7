package ru.geekbrains.android3_7.model.api;

import java.util.List;

import io.reactivex.Observable;
import ru.geekbrains.android3_7.model.entity.User;
import ru.geekbrains.android3_7.model.entity.UserRepository;

/**
 * Created by stanislav on 3/12/2018.
 */

public interface IUserRepo
{
    Observable<User> getUser(String username);
    Observable<List<UserRepository>> getUserRepos(User user);

}

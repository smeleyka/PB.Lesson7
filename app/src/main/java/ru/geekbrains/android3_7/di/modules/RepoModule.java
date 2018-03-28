package ru.geekbrains.android3_7.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.geekbrains.android3_7.model.api.ApiService;
import ru.geekbrains.android3_7.model.api.UserRepo;
import ru.geekbrains.android3_7.model.cache.AACache;
import ru.geekbrains.android3_7.model.cache.ICache;


@Module(includes = {ApiModule.class})
public class RepoModule
{
    @Provides
    public ICache cache()
    {
        return new AACache();
    }

    @Provides
    public UserRepo userRepo(ApiService api, ICache cache)
    {
        return new UserRepo(api, cache);
    }
}

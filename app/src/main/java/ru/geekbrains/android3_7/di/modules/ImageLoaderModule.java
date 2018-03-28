package ru.geekbrains.android3_7.di.modules;

import android.widget.ImageView;

import dagger.Module;
import dagger.Provides;
import ru.geekbrains.android3_7.model.api.ApiService;
import ru.geekbrains.android3_7.model.api.UserRepo;
import ru.geekbrains.android3_7.model.cache.ICache;
import ru.geekbrains.android3_7.model.image.ImageLoader;
import ru.geekbrains.android3_7.model.image.android.ImageLoaderGlide;


@Module(includes = {ApiModule.class})
public class ImageLoaderModule
{
    @Provides
    public ImageLoader<ImageView> imageLoader()
    {
        return new ImageLoaderGlide();
    }
}

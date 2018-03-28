package ru.geekbrains.android3_7.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.realm.annotations.RealmClass;
import ru.geekbrains.android3_7.model.api.UserRepo;
import ru.geekbrains.android3_7.model.entity.User;
import ru.geekbrains.android3_7.view.MainView;
import ru.geekbrains.android3_7.view.RepoRowView;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> implements IRepoListPresenter
{
    private static final String TAG = "MainPresenter";
    private Scheduler scheduler;

    @Inject public UserRepo userRepo;

    private User user;

    public MainPresenter(Scheduler scheduler)
    {
        this.scheduler = scheduler;
    }

    @Override
    protected void onFirstViewAttach()
    {
        getViewState().checkPermissions();
    }

    public void loadInfo(String username)
    {
        userRepo.getUser(username).subscribe(user -> {
            this.user = user;
            userRepo.getUserRepos(user)
                    .observeOn(scheduler)
                    .subscribe(userRepositories -> {
                       this.user.setRepos(userRepositories);
                        getViewState().hideLoading();
                        getViewState().showAvatar(user.getAvatarUrl());
                        getViewState().setUsername(user.getLogin());
                        getViewState().updateRepoList();
                    }, throwable -> {
                        Log.e(TAG, "Failed to get user repos", throwable);
                        String message = throwable.getMessage();
                        getViewState().showError(message);
                        getViewState().hideLoading();
                    });
        }, throwable -> {
            //Log.e(TAG, "Failed to get user", throwable);
            String message = throwable.getMessage();
            getViewState().showError(message);
            getViewState().hideLoading();
        });
    }

    public void onPermissionsGranted()
    {

        loadInfo("smeleyka");
    }

    @Override
    public void bindRepoListRow(int pos, RepoRowView rowView)
    {
        if(user != null)
        {
            rowView.setTitle(user.getRepos().get(pos).getName());
        }
    }

    @Override
    public int getRepoCount()
    {
        return user == null ? 0 : user.getRepos().size();
    }
}

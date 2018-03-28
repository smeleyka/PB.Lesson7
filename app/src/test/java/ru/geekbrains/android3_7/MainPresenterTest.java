package ru.geekbrains.android3_7;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;
import ru.geekbrains.android3_7.model.api.UserRepo;
import ru.geekbrains.android3_7.model.entity.User;
import ru.geekbrains.android3_7.presenter.MainPresenter;
import ru.geekbrains.android3_7.view.MainView;

public class MainPresenterTest
{

    TestScheduler testScheduler = new TestScheduler();
    @Mock MainView view;
    @Mock UserRepo userRepo;

    @BeforeClass
    public static void setupClass() {}

    @AfterClass
    public static void tearDownClass() {}


    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown()
    {

    }

    @Test
    public void onFirstViewAttach() throws Exception
    {
        MainPresenter presenter = new MainPresenter(testScheduler);
        presenter.attachView(view);
        Mockito.verify(view).checkPermissions();
    }


    @Test
    public void onPermissonGranted() throws Exception
    {
        User user = new User("smeleyka", null);

        Mockito.when(userRepo.getUser(user.getLogin())).thenReturn(Observable.just(user));
        Mockito.when(userRepo.getUserRepos(user)).thenReturn(Observable.just(new ArrayList<>()));

        MainPresenter presenter = Mockito.spy(new MainPresenter(testScheduler));

        presenter.userRepo = userRepo;
        presenter.attachView(view);
        presenter.onPermissionsGranted();

        Mockito.verify(presenter).loadInfo(user.getLogin());
    }


    @Test
    public void loadInfo() throws Exception
    {
        loadInfoSuccess();
        loadInfoFail();
    }

    private void loadInfoSuccess() throws Exception
    {
        MainPresenter presenter = Mockito.spy(new MainPresenter(testScheduler));
        User user = new User("smeleyka", null);
        Mockito.when(userRepo.getUser(user.getLogin())).thenReturn(Observable.just(user));
        Mockito.when(userRepo.getUserRepos(user)).thenReturn(Observable.just(new ArrayList<>()));
        presenter.userRepo = userRepo;
        presenter.attachView(view);

        presenter.loadInfo(user.getLogin());

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);

        Mockito.verify(presenter).loadInfo(user.getLogin());
        Mockito.verify(view).hideLoading();
        Mockito.verify(view).showAvatar(null);
        Mockito.verify(view).setUsername(user.getLogin());
        Mockito.verify(view).updateRepoList();
    }

    private void loadInfoFail() throws Exception
    {
        String error = "some error";

        MainPresenter presenter = Mockito.spy(new MainPresenter(testScheduler));
        User user = new User("smeleyka", null);
        Mockito.when(userRepo.getUser(user.getLogin())).thenReturn(Observable.error(new RuntimeException(error)));
        Mockito.when(userRepo.getUserRepos(user)).thenReturn(Observable.just(new ArrayList<>()));
        presenter.userRepo = userRepo;
        presenter.attachView(view);

        presenter.loadInfo(user.getLogin());

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);

        Mockito.verify(presenter).loadInfo(user.getLogin());
        Mockito.verify(view, Mockito.atLeast(2)).hideLoading();
        Mockito.verify(view).showError(error);
    }
}

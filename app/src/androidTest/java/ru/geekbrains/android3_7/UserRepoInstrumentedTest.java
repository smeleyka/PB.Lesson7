package ru.geekbrains.android3_7;

import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import io.reactivex.observers.TestObserver;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import ru.geekbrains.android3_7.di.DaggerRepoTestComponent;
import ru.geekbrains.android3_7.di.RepoTestComponent;
import ru.geekbrains.android3_7.di.modules.ApiModule;
import ru.geekbrains.android3_7.model.api.UserRepo;
import ru.geekbrains.android3_7.model.cache.AACache;
import ru.geekbrains.android3_7.model.entity.User;
import ru.geekbrains.android3_7.model.entity.UserRepository;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UserRepoInstrumentedTest
{
    private static MockWebServer webServer;

    @Inject UserRepo userRepo;

    @BeforeClass
    public static void setupClass() throws IOException
    {
        webServer = new MockWebServer();
        webServer.start();
    }

    @AfterClass
    public static void tearDownClass() throws IOException
    {
        webServer.shutdown();
    }

    @Before
    public void setup()
    {
        RepoTestComponent component = DaggerRepoTestComponent
                .builder()
//                .apiModule(new ApiModule(){
//                    @Override
//                    public String endpoint()
//                    {
//                        return webServer.url("/").toString();
//                    }
//                })
                .build();

        component.inject(this);
    }


//    {
//        "login": "smeleyka",
//        "id": 26659727,
//        "avatar_url": "https://avatars2.githubusercontent.com/u/26659727?v=4",
//        "gravatar_id": "",
//        "url": "https://api.github.com/users/smeleyka",
//        "html_url": "https://github.com/smeleyka",
//        "followers_url": "https://api.github.com/users/smeleyka/followers",
//        "following_url": "https://api.github.com/users/smeleyka/following{/other_user}",
//        "gists_url": "https://api.github.com/users/smeleyka/gists{/gist_id}",
//        "starred_url": "https://api.github.com/users/smeleyka/starred{/owner}{/repo}",
//        "subscriptions_url": "https://api.github.com/users/smeleyka/subscriptions",
//        "organizations_url": "https://api.github.com/users/smeleyka/orgs",
//        "repos_url": "https://api.github.com/users/smeleyka/repos",
//        "events_url": "https://api.github.com/users/smeleyka/events{/privacy}",
//        "received_events_url": "https://api.github.com/users/smeleyka/received_events",
//        "type": "User",
//        "site_admin": false,
//        "name": null,
//        "company": null,
//        "blog": "",
//        "location": null,
//        "email": null,
//        "hireable": null,
//        "bio": null,
//        "public_repos": 58,
//        "public_gists": 0,
//        "followers": 0,
//        "following": 0,
//        "created_at": "2017-03-24T19:33:47Z",
//        "updated_at": "2018-03-14T13:09:40Z"
//    }

    String login = "somelogin";
    String avatarUrl = "someurl";

    @Test
    public void getUser() throws Exception
    {
        webServer.enqueue(createMockResponse(login, avatarUrl));
        TestObserver<User> observer = new TestObserver<>();
        userRepo.getUser(login).subscribe(observer);
        observer.awaitTerminalEvent();
        observer.assertValueCount(1);
    }


    @Test
    public void getUserRepo() throws Exception
    {
        TestObserver<List<UserRepository>> observer = new TestObserver<>();
        userRepo.getUser(login).subscribe(user -> {
            userRepo.getUserRepos(user).subscribe(observer);
        });
        observer.awaitTerminalEvent();
        observer.assertValueCount(1);

    }


    private MockResponse createMockResponse(String login, String avatarUrl)
    {
        String body = "{\"login\":\"" + login + "\", \"avatar_url\":\"" + avatarUrl +"\"}";
        return new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody(body);
    }
}

package ru.geekbrains.android3_7;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.observers.TestObserver;
import ru.geekbrains.android3_7.model.cache.AACache;
import ru.geekbrains.android3_7.model.entity.User;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AACacheInstrumentedTest
{
    @Test
    public void putGetUser() throws Exception
    {
        AACache aaCache = new AACache();
        User user = new User("somename", "someurl");

        aaCache.putUser(user);

        TestObserver<User> observer = new TestObserver<>();
        aaCache.getUser(user.getLogin()).subscribe(observer);

        observer.awaitTerminalEvent();

        observer.assertValueCount(1);

        User receivedUser = observer.values().get(0);

        assertEquals(receivedUser, user);
    }
}

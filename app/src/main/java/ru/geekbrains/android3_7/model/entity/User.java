package ru.geekbrains.android3_7.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stanislav on 3/12/2018.
 */

public class User
{
    String login;
    String avatarUrl;
    List<UserRepository> repos = new ArrayList<>();

    public User(String login, String avatarUrl)
    {
        this.avatarUrl = avatarUrl;
        this.login = login;
        this.repos = new ArrayList<>();
    }

    public String getAvatarUrl()
    {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl)
    {
        this.avatarUrl = avatarUrl;
    }

    public String getLogin()
    {
        return login;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public List<UserRepository> getRepos()
    {
        return repos;
    }

    public void setRepos(List<UserRepository> repos)
    {
        this.repos = repos;
    }

    @Override
    public boolean equals(Object obj)
    {
        User other = (User) obj;
        return this.login.equals(other.login) && this.avatarUrl.equals(other.avatarUrl);
    }

    @Override
    public int hashCode()
    {
        return login.hashCode();
    }
}

package ru.netology.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.clearBrowserCookies;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.SQLHelper.cleanDB;

public class TestLogin {
    @AfterAll
    static void cleaner() {
        cleanDB();
    }

    @Test
    void shouldSuccessLogin() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authUser = DataHelper.getAuthUser();
        var verifyPage = loginPage.validLogin(authUser);
        verifyPage.verifyPageVisibility();
        var verifyCode = SQLHelper.getVerifyCode();
        verifyPage.validVerify(verifyCode.getCode());
    }

    @Test
    void shouldRandomUser() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getRandomUser();
        loginPage.validLogin(authInfo);
        loginPage.getError();
    }

    @Test
    void shouldInvalidLogin() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authUser = new DataHelper.AuthUser(DataHelper.getRandomUser().getLogin(),
                DataHelper.getAuthUser().getPassword());
        loginPage.validLogin(authUser);
        loginPage.getError();
    }

    @Test
    void shouldInvalidPass() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authUser = new DataHelper.AuthUser(DataHelper.getAuthUser().getLogin(),
                DataHelper.getRandomUser().getPassword());
        loginPage.validLogin(authUser);
        loginPage.getError();
    }

    @Test
    void shouldInvalidVerifyCode() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authUser = DataHelper.getAuthUser();
        var verifyPage = loginPage.validLogin(authUser);
        verifyPage.verifyPageVisibility();
        var verifyCode = DataHelper.getRandomVerifyCode().getCode();
        verifyPage.verify(verifyCode);
        verifyPage.getError();
    }

    @Test
    void shouldPassThreeTimes() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authUserOne = new DataHelper.AuthUser(DataHelper.getAuthUser().getLogin(),
                DataHelper.getRandomUser().getPassword());
        loginPage.validLogin(authUserOne);
        loginPage.getError();
        loginPage.clean();
        clearBrowserCookies();
        var authUserTwo = new DataHelper.AuthUser(DataHelper.getAuthUser().getLogin(),
                DataHelper.getRandomUser().getPassword());
        loginPage.validLogin(authUserTwo);
        loginPage.getError();
        loginPage.clean();
        clearBrowserCookies();
        var authUserThree = new DataHelper.AuthUser(DataHelper.getAuthUser().getLogin(),
                DataHelper.getRandomUser().getPassword());
        loginPage.validLogin(authUserThree);
        loginPage.getBlock();
    }

}

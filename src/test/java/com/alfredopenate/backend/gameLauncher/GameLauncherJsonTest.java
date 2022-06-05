package com.alfredopenate.backend.gameLauncher;

import com.alfredopenate.backend.globalSettings;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class GameLauncherJsonTest extends globalSettings {
    @DataProvider(name = "testLocaleCurrencyMarketMatchesInput")
    public Object[][] createGameLauncherTestData() {

        return new String[][] {
                {"en_GB","EUR", "GB"},
                {"es_ES","EUR", "ES"},
                {"en_US","EUR", "US"}
        };
    }

    @Test(dataProvider = "testLocaleCurrencyMarketMatchesInput")
    public void testLocaleCurrencyMarketMatchesInput(String locale, String currency, String market)
    {
        Response r =    given()
                        .queryParam("brand", "unibet")
                        .queryParam("locale", locale)
                        .when()
                        .get("/kambi-rest-api/gameLauncher2.json");

        JsonPath jsonResponse = r.jsonPath();

        //Assertions
        assertThat(r.statusCode(), is(200));

        assertThat(jsonResponse.get("lang"), is(locale));
        assertThat(jsonResponse.get("currency"), is(currency));
        assertThat(jsonResponse.get("market"), is(market));
    }
}

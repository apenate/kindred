package com.alfredopenate.backend.azwidget;

import com.alfredopenate.backend.globalSettings;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matcher;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;


public class AZWidgetTest extends globalSettings {

    private static boolean exceptionOccurred = false;
    private static String failedScenarios = "";
    @DataProvider(name = "testAZWidget")
    public Object[][] createAZWidgetTestData() {

        return new String[][] {
                {"https://www.unibet.co.uk"},
                {"https://www.unibet.se"},
                {"https://www.unibet.com"}
        };
    }

    @Test(dataProvider = "testAZWidget")
    public void testAZWidget(String host)
    {
        Response r =    given()
                        .when()
                        .get(host + "/sportsbook-feeds/views/sports/a-z");

        //Parsing response to JSON
        JsonPath jsonResponse = r.jsonPath();

        //Extracting Sport Names, boCounts and iconUrls from response, saving to an ArrayList for looping later
        String jsonLocator = "layout.sections.widgets.find{it.size() > 0}.sports";
        ArrayList<?> names = new ArrayList<>((Collection<?>)jsonResponse.getList(jsonLocator + ".name").get(0));
        ArrayList<?> boCounts = new ArrayList<>((Collection<?>)jsonResponse.getList(jsonLocator + ".boCount").get(0));
        ArrayList<?> iconUrls = new ArrayList<>((Collection<?>)jsonResponse.getList(jsonLocator + ".iconUrl").get(0));

        //Assertions
        Assert.assertEquals(r.statusCode(), 200);

        for (Object name: names) {
            _assertThat(name + " is alphanumerical", name.toString(), matchesPattern("^\\w+( \\w+)*$"));
        }

        for (Object boCount: boCounts) {
            _assertThat(boCount + " is an integer", boCount, instanceOf(Integer.class));
        }

        for (Object iconUrl: iconUrls) {
            Response res = given().when().get(iconUrl.toString());
            _assertThat("Status code for " + iconUrl, res.statusCode(), is(200));
            _assertThat("Content Type for " + iconUrl, res.header( "Content-Type"), is("image/svg+xml"));
        }

        if(exceptionOccurred){fail(failedScenarios);}

    }

    //Customising assertThat() method, so it does not exit on assertion failure
    private <T> void _assertThat(String reason, T actual, Matcher<? super T> matcher)
    {
        try{
            assertThat(reason, actual, matcher);
        }
        catch(AssertionError e)
        {
            exceptionOccurred = true;
            failedScenarios += e.getMessage() + "\n";
        }
    }
}

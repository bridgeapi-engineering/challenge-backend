package controllers;

import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class UserControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void ping_should_respond_http_200_for_an_existing_account() {
        //TODO a mock should be used to mock BridgeClient component
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/user/balance");

        Result result = route(app, request);
        assertEquals(OK, result.status());
        assertTrue(contentAsString(result).contains("rounded_amount"));
    }
}

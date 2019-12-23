package controllers;

import models.RoundedSum;
import play.Logger;
import play.libs.Json;
import play.mvc.*;
import services.BridgeClient;

import javax.inject.Inject;

/**
 * MyController.myMethod is called when requesting GET /user/balance (see routes file)
 *
 * You can try it by running curl -X GET localhost:9000/user/balance
 *
 * The BridgeClient has been injected and ready for use.
 */
public class UserController extends Controller {

    private final BridgeClient bridgeClient;

    @Inject
    public UserController(BridgeClient bridgeClient) {
        this.bridgeClient = bridgeClient;
    }

    /**
     * Returns the sum of a user's checking and saving accounts, rounded to the upper hundred
     * @return an HTTP response
     */
    public Result getCheckingAndSavingAccountsSum() {
        try {
            return ok(Json.toJson(new RoundedSum(bridgeClient.getCheckingAndSavingAccountsSum())));
        } catch (RuntimeException e) {
            Logger.error(e.getMessage(), e);
            return status(503, "Service Unavailable");
        }
    }
}

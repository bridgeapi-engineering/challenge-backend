package services;

import com.typesafe.config.Config;
import models.AuthenticateResponse;
import models.GetAccountsResponse;
import org.springframework.util.CollectionUtils;
import play.libs.Json;
import play.libs.ws.WSClient;

import javax.inject.Inject;
import java.util.Optional;

import static play.mvc.Http.Status.OK;

/**
 * Bridge is Bankin's SaaS API. This service is where the calls to the API should be implemented.
 */
public class BridgeClient {

    private final WSClient wsClient;
    private final String baseUrl;
    private final String apiVersion;
    private final String apiClientId;
    private final String apiClientSecret;

    // these are hardcoded for simplicity's sake
    private static final String USER_EMAIL = "user1@mail.com";
    private static final String USER_PASSWORD = "a!Strongp#assword1";

    @Inject
    public BridgeClient(WSClient wsClient, Config config) {
        this.wsClient = wsClient;
        this.baseUrl = config.getString("bankin.api.baseUrl");
        this.apiVersion = config.getString("bankin.api.version");
        this.apiClientId = config.getString("bankin.api.app.clientId");
        this.apiClientSecret = config.getString("bankin.api.app.clientSecret");
    }

    /**
     * This method is "complete" and doesn't need editing, except if you feel some things could be improved (there
     * is no trap)
     */
    private Optional<AuthenticateResponse> authenticateUser(String email, String password) {
        return wsClient.url(baseUrl + "/authenticate")
                .addHeader("Bankin-Version", apiVersion)
                .addQueryParameter("client_id", apiClientId)
                .addQueryParameter("client_secret", apiClientSecret)
                .addQueryParameter("email", email)
                .addQueryParameter("password", password)
                .post("")
                .thenApply(response -> {
                    if (response.getStatus() == OK) {
                        return Optional.of(Json.fromJson(response.asJson(), AuthenticateResponse.class));
                    }
                    return Optional.<AuthenticateResponse>empty();
                })
                .toCompletableFuture()
                .join();
    }

    /**
     * Returns the sum of a user's checking and saving accounts, rounded to the upper hundred
     * @return amount rounded to the upper hundred
     */
    public double getCheckingAndSavingAccountsSum() {
        Optional<AuthenticateResponse> maybeAccessToken = authenticateUser(USER_EMAIL, USER_PASSWORD);

        if (maybeAccessToken.isPresent()) {
            return wsClient.url(baseUrl + "/accounts")
                    .addHeader("Bankin-Version", apiVersion)
                    .addHeader("Authorization", "Bearer " + maybeAccessToken.get().accessToken)
                    .addQueryParameter("limit", String.valueOf(10))
                    .addQueryParameter("client_id", apiClientId)
                    .addQueryParameter("client_secret", apiClientSecret)
                    .get()
                    .thenApply(response -> {
                        Optional<GetAccountsResponse> getAccountsResponse = Optional.ofNullable(Json.fromJson(response.asJson(), GetAccountsResponse.class));
                        if (getAccountsResponse.isPresent() && response.getStatus() == OK) {
                            if (!CollectionUtils.isEmpty(getAccountsResponse.get().accounts)) {
                                return getAccountsResponse.get().accounts.stream()
                                        .filter(account -> account != null&& account.balance != null)
                                        .mapToDouble(account -> account.balance)
                                        .sum();
                            }
                        }
                        throw new RuntimeException("Cannot retrieve balances from supplied response");
                    })
                    .toCompletableFuture()
                    .join();
        }

        throw new RuntimeException("Cannot retrieve access token");
    }
}

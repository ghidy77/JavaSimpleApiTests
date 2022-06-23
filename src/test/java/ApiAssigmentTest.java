
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ApiAssigmentTest {

    private final static String BASE_URL = "https://some-example-api.com/v1/tests/"; //{stationId}

    @Test(description = "Get Version test")
    public void getVersionTest() throws IOException {

        String getVersionPayload = "{ \"command\": \"getVersion\", \"payload\": null }";

        HttpResponse response = post(getVersionPayload).get(0);
        int responseCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(responseCode, HttpStatus.SC_OK, "Invalid response code");

        String responseStr = EntityUtils.toString(response.getEntity());

        JsonElement responseJson = new JsonParser().parse(responseStr);
        Assert.assertTrue(responseJson.isJsonObject(), "Invalid response");
        Assert.assertTrue(responseJson.getAsJsonObject().has("result"), "Result not present in response");

        double resultVersion = responseJson.getAsJsonObject().get("result").getAsDouble();
        double expectedResultVersion = 1.6;
        Assert.assertTrue(resultVersion > expectedResultVersion, "Incorrect version. Expected greater than: " + expectedResultVersion +", Actual: " + resultVersion);
    }

    @Test(description = "Get Interval test")
    public void getIntervalTest() throws IOException {

        String getVersionPayload = "{ \"command\": \"getInterval\", \"payload\": null }";

        HttpResponse response = post(getVersionPayload).get(0);
        int responseCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(responseCode, HttpStatus.SC_OK, "Invalid response code");

        String responseStr = EntityUtils.toString(response.getEntity());

        JsonElement responseJson = new JsonParser().parse(responseStr);
        Assert.assertTrue(responseJson.isJsonObject(), "Invalid response");
        Assert.assertTrue(responseJson.getAsJsonObject().has("result"), "Result not present in response");

        int resultVersion = responseJson.getAsJsonObject().get("result").getAsInt();
        int expectedResultVersion = 1;
        Assert.assertTrue(resultVersion > expectedResultVersion, "Incorrect interval. Expected: " + expectedResultVersion +", Actual: " + resultVersion);
    }

    @Test(description = "Set Values test")
    public void setValuesTest() throws IOException {

        String setValuesPayload = "{ \"command\": \"setValues\", \"payload\": 1 }";

        HttpResponse response = post(setValuesPayload).get(0);
        int responseCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(responseCode, HttpStatus.SC_OK, "Invalid response code");

        String responseStr = EntityUtils.toString(response.getEntity());

        JsonElement responseJson = new JsonParser().parse(responseStr);
        Assert.assertTrue(responseJson.isJsonObject(), "Invalid response");
        Assert.assertTrue(responseJson.getAsJsonObject().has("result"), "Result not present in response");

        String result = responseJson.getAsJsonObject().get("result").getAsString();
        Assert.assertEquals(result, "FAILED", "Invalid Result");
    }

    public Map<Integer, HttpResponse> post(String body) throws IOException {

        Map stationsResponses = new HashMap();

        for (int i=1;i<=5; i++){
            HttpPost postRequest = new HttpPost(BASE_URL+i);
            postRequest.setEntity(new StringEntity(body));
            postRequest.addHeader("Content-Type","application/json");
            postRequest.addHeader("Accept","*/*");
            stationsResponses.put(i, HttpClients.custom().build().execute(postRequest));
        }

        return stationsResponses;
    }
}

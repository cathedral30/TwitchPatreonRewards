package info.cooper.TwitchPatreonRewards.patreon;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class PatreonRequests {

    public JSONObject validateOAuthCode(String code, String clientId, String secret, String uri) throws IOException {
        URL url = new URL("https://www.patreon.com/api/oauth2/token");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        Map<String, String> parameters = new HashMap<>();
        parameters.put("code", code);
        parameters.put("grant_type", "authorization_code");
        parameters.put("client_id", clientId);
        parameters.put("client_secret", secret);
        parameters.put("redirect_uri", uri);

        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(getParamsString(parameters));
        out.flush();
        out.close();

        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        int status = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();

        String strContent = String.valueOf(content);
        return new JSONObject(strContent);
    }

    public JSONObject refreshOAuthToken(String refreshToken, String clientId, String secret) throws IOException {
        URL url = new URL("https://www.patreon.com/api/oauth2/token");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        Map<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", "refresh_token");
        parameters.put("refresh_token", refreshToken);
        parameters.put("client_id", clientId);
        parameters.put("client_secret", secret);

        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(getParamsString(parameters));
        out.flush();
        out.close();

        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        int status = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();

        String strContent = String.valueOf(content);
        return new JSONObject(strContent);
    }

    public PatreonUser getPatreonUser(String token) throws IOException {
        URL url = new URL("https://patreon.com/api/oauth2/api/current_user");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + token);

        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        int status = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();

        String strContent = String.valueOf(content);
        JSONObject data = new JSONObject(strContent).getJSONObject("data");
        Long id = data.getLong("id");
        JSONObject attributes = data.getJSONObject("attributes");
        return new PatreonUser(
                id,
                attributes.getString("about"),
                attributes.getString("email"),
                attributes.getString("first_name"),
                attributes.getString("last_name"),
                attributes.getString("image_url"),
                attributes.getString("thumb_url"),
                attributes.getString("url")
        );
    }

    // https://www.baeldung.com/java-http-request
    public static String getParamsString(Map<String, String> params) {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }
}

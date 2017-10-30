package com.sportspartner.unittest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sportspartner.main.Bootstrap;
import com.sportspartner.service.*;
import com.sportspartner.controllers.*;
import org.junit.*;
import spark.utils.IOUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static spark.Spark.ipAddress;
import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

public class ProfileTest {
    HttpURLConnection connection = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception{
        ipAddress(Bootstrap.IP_ADDRESS);
        port(Bootstrap.PORT);
        staticFileLocation("/public");
        new ProfileController(new ProfileService());
        Thread.sleep(4000);
    }

    @AfterClass
    public static void tearDownAfterClass(){

    }

    @Before
    public void setUp(){}

    @After
    public void teardown(){
    }

    @Test
    // Test get a person's profile
    public void testProfileSuccess(){
        String responseBody = new String();
        String API_CONTEXT = "/api.sportspartner.com/v1";
        String userId = "zihao@jhu.edu";

        try{
            URL url = new URL("http", Bootstrap.IP_ADDRESS, Bootstrap.PORT, API_CONTEXT + "/profile/" + userId);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            responseBody = IOUtils.toString(connection.getInputStream());
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

        assertEquals("{\"response\":\"true\",\"profile\":{\"userId\":\"zihao@jhu.edu\",\"userName\":\"zihao xiao\",\"address\":\"Guangdong\",\"gender\":\"male\",\"age\":23,\"punctuality\":4.5,\"participation\":5.0,\"iconPath\":\"server/res/icon/zihao@jhu.edu\",\"iconUUID\":\"ASD123\"}}", responseBody);
    }

    @Test
    // Test get an non-existed person's profile
    public void testProfileFailure(){
        String responseBody = new String();
        String API_CONTEXT = "/api.sportspartner.com/v1";
        String userId = "frog";

        try{
            URL url = new URL("http", Bootstrap.IP_ADDRESS, Bootstrap.PORT, API_CONTEXT + "/profile/" + userId);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            responseBody = IOUtils.toString(connection.getInputStream());
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

        assertEquals("{\"response\":\"false\",\"message\":\"No such user\"}", responseBody);
    }

    @Test
    // Test update a person's profile
    public void testProfileUpdate(){
        JsonObject parameters = new JsonObject();
        String responseBody = new String();
        String API_CONTEXT = "/api.sportspartner.com/v1";
        String userId = "xuanzhang666@jhu.edu";

        try{
            URL url = new URL("http", Bootstrap.IP_ADDRESS, Bootstrap.PORT, API_CONTEXT + "/profile/" + userId);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            JsonParser parser = new JsonParser();
            parameters = parser.parse("{\"userId\":\"xuanzhang666@jhu.edu\",\"key\":\"007\",\"profile\":{\"userId\":\"xuanzhang666@jhu.edu\",\"userName\":\"xuanzhang\",\"address\":\"Inner Mongolia\",\"gender\":\"female\",\"age\":23,\"punctuality\":4.5,\"participation\":5.0,\"iconPath\":\"server/res/icon/xuanzhang666@jhu.edu\", \"iconUUID\": \"897WEM\"}}").getAsJsonObject();
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            try(DataOutputStream wr = new DataOutputStream( connection.getOutputStream())){
                wr.writeBytes( parameters.toString());
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            responseBody = IOUtils.toString(connection.getInputStream());
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

        assertEquals("{\"response\":\"true\",\"authorization\":\"true\"}", responseBody);
    }

    @Test
    // Test fail to update a person's profile when the user has no authorization
    public void testProfileUpdateFailure(){
        JsonObject parameters = new JsonObject();
        String responseBody = new String();
        String API_CONTEXT = "/api.sportspartner.com/v1";
        String userId = "xuanzhang666@jhu.edu";

        try{
            URL url = new URL("http", Bootstrap.IP_ADDRESS, Bootstrap.PORT, API_CONTEXT + "/profile/" + userId);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            JsonParser parser = new JsonParser();
            parameters = parser.parse("{\"userId\":\"xuan@jhu.edu\",\"key\":\"005\",\"profile\":{\"userId\":\"xuanzhang666@jhu.edu\",\"userName\":\"xuanzhang\",\"address\":\"Inner Mongolia\",\"gender\":\"female\",\"age\":23,\"punctuality\":4.5,\"participation\":5.0,\"iconPath\":\"server/res/icon/xuanzhang666@jhu.edu\", \"iconUUID\": \"897WEM\"}}").getAsJsonObject();
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            try(DataOutputStream wr = new DataOutputStream( connection.getOutputStream())){
                wr.writeBytes( parameters.toString());
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            responseBody = IOUtils.toString(connection.getInputStream());
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

        assertEquals("{\"response\":\"false\",\"message\":\"Lack authorization to update profile\",\"authorization\":\"false\"}", responseBody);
    }

    @Test
    // Test get a user outline successfully
    public void testUserOutlineSuccess(){
        String responseBody = new String();
        String API_CONTEXT = "/api.sportspartner.com/v1";
        String userId = "zihao@jhu.edu";

        try{
            URL url = new URL("http", Bootstrap.IP_ADDRESS, Bootstrap.PORT, API_CONTEXT + "/profile/outline/" + userId);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            responseBody = IOUtils.toString(connection.getInputStream());
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

        assertEquals("{\"response\":\"true\",\"userOutline\":{\"userId\":\"zihao@jhu.edu\",\"userName\":\"zihao xiao\",\"iconPath\":\"server/res/icon/zihao@jhu.edu\",\"iconUUID\":\"ASD123\"}}", responseBody);
    }

    @Test
    // Test get an non-existed person's profile outline
    public void testUserOutlineFailure(){
        String responseBody = new String();
        String API_CONTEXT = "/api.sportspartner.com/v1";
        String userId = "frog";

        try{
            URL url = new URL("http", Bootstrap.IP_ADDRESS, Bootstrap.PORT, API_CONTEXT + "/profile/outline/" + userId);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            responseBody = IOUtils.toString(connection.getInputStream());
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

        assertEquals("{\"response\":\"false\",\"message\":\"No such user\"}", responseBody);
    }

    @Test
    // Test get all sports
    public void testGetAllSports(){
        String responseBody = new String();
        String API_CONTEXT = "/api.sportspartner.com/v1";

        try{
            URL url = new URL("http", Bootstrap.IP_ADDRESS, Bootstrap.PORT, API_CONTEXT + "/sports");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            responseBody = IOUtils.toString(connection.getInputStream());
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

        assertEquals("{\"response\":\"true\",\"sports\":[{\"sportId\":\"002\",\"sportName\":\"Basketball\",\"sportIconPath\":\"/image/sporticon/002.png\",\"sportIconUUID\":\"01fb3200-bc1c-11e7-abc4-cec278b6b50a\"},{\"sportId\":\"001\",\"sportName\":\"Swimming\",\"sportIconPath\":\"/image/sporticon/001.png\",\"sportIconUUID\":\"01fb2db4-bc1c-11e7-abc4-cec278b6b50a\"},{\"sportId\":\"003\",\"sportName\":\"Tennis\",\"sportIconPath\":\"/image/sporticon/003.png\",\"sportIconUUID\":\"01fb3462-bc1c-11e7-abc4-cec278b6b50a\"},{\"sportId\":\"004\",\"sportName\":\"Baseball\",\"sportIconPath\":\"/image/sporticon/004.png\",\"sportIconUUID\":\"01fb369c-bc1c-11e7-abc4-cec278b6b50a\"},{\"sportId\":\"005\",\"sportName\":\"Soccer\",\"sportIconPath\":\"/image/sporticon/005.png\",\"sportIconUUID\":\"01fb389a-bc1c-11e7-abc4-cec278b6b50a\"}]}", responseBody);
    }

    @Test
    // Test get all interests of a user
    public void testGetAllInterestsSuccess(){
        String responseBody = new String();
        String API_CONTEXT = "/api.sportspartner.com/v1";
        String userId = "u1";

        try{
            URL url = new URL("http", Bootstrap.IP_ADDRESS, Bootstrap.PORT, API_CONTEXT + "/interests/" + userId);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            responseBody = IOUtils.toString(connection.getInputStream());
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

        assertEquals("{\"interest\":\"Swimming,Tennis\"}", responseBody);
    }

    @Test
    // Test get interests of an non-existed user
    public void testGetAllInterestsFailure(){
        String responseBody = new String();
        String API_CONTEXT = "/api.sportspartner.com/v1";
        String userId = "frog";

        try{
            URL url = new URL("http", Bootstrap.IP_ADDRESS, Bootstrap.PORT, API_CONTEXT + "/interests/" + userId);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            responseBody = IOUtils.toString(connection.getInputStream());
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

        assertEquals("{\"response\":\"false\",\"message\":\"No such user\"}", responseBody);
    }

    @Test
    // Test update a user's interests
    public void testInterestsUpdate(){
        JsonObject parameters = new JsonObject();
        String responseBody = new String();
        String API_CONTEXT = "/api.sportspartner.com/v1";
        String userId = "u1";

        try{
            URL url = new URL("http", Bootstrap.IP_ADDRESS, Bootstrap.PORT, API_CONTEXT + "/interests/" + userId);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            JsonParser parser = new JsonParser();
            parameters = parser.parse("{\"interests\":\"001,003\"}").getAsJsonObject();
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            try(DataOutputStream wr = new DataOutputStream( connection.getOutputStream())){
                wr.writeBytes( parameters.toString());
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            responseBody = IOUtils.toString(connection.getInputStream());
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

        assertEquals("{\"response\":\"true\"}", responseBody);
    }

    @Test
    // Test get profile comments
    public void testGetProfileCommentsFailure() {
        String responseBody = new String();
        String API_CONTEXT = "/api.sportspartner.com/v1";
        String userId = "u1";

        try {
            URL url = new URL("http", Bootstrap.IP_ADDRESS, Bootstrap.PORT, API_CONTEXT + "/profile_comments/" + userId);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            responseBody = IOUtils.toString(connection.getInputStream());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }


        JsonObject responseJson = new Gson().fromJson(responseBody, JsonObject.class);
        String response = responseJson.get("response").getAsString();
        assertEquals("true", response);
    }

    @Test
    // Test get profile comments for an non-existed user
    public void testGetProfileCommentsSuccess() {
        String responseBody = new String();
        String API_CONTEXT = "/api.sportspartner.com/v1";
        String userId = "frog";

        try {
            URL url = new URL("http", Bootstrap.IP_ADDRESS, Bootstrap.PORT, API_CONTEXT + "/profile_comments/" + userId);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            responseBody = IOUtils.toString(connection.getInputStream());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        assertEquals("{\"response\":\"false\",\"message\":\"No such user\"}", responseBody);
    }

    @Test
    // Test leave a new profile comment without authorization: author is not in the same activity with the owner of the uset
    public void testNewProfileCommentFailure() {
        JsonObject parameters = new JsonObject();
        String responseBody = new String();
        String API_CONTEXT = "/api.sportspartner.com/v1";
        String userId = "u1";

        try {
            URL url = new URL("http", Bootstrap.IP_ADDRESS, Bootstrap.PORT, API_CONTEXT + "/profile_comments/" + userId);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            JsonParser parser = new JsonParser();
            parameters = parser.parse("{\"authorId\":\"zihao@jhu.edu\", \"key\":\"666\", \"activityId\": \"02\", \"comment\": {\"userId\":\"u1\",\"commentId\":\"005\",\"authorId\":\"Dog\",\"authorName\":\"zli86@jhu.edu\",\"time\":\"Oct 22, 2017 10:06:06 PM\",\"content\":\"A good soccer player.\"}}").getAsJsonObject();
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            try(DataOutputStream wr = new DataOutputStream( connection.getOutputStream())){
                wr.writeBytes( parameters.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            responseBody = IOUtils.toString(connection.getInputStream());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        assertEquals("{\"response\":\"false\",\"message\":\"Lack authorization to leave a comment\"}", responseBody);
    }


}

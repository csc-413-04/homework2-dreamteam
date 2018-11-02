package main.java;

import com.mongodb.*;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {

        //This creates the connection to the MangoDB database.
        MongoClient mongo = new MongoClient("localhost", 27017);
        DB db = mongo.getDB("REST2");

        //This makes a collection accessible

        DBCollection colusers = db.getCollection("users");
        DBCollection colauth = db.getCollection("auth");

        // staticFiles.externalLocation("public");
        // http://sparkjava.com/documentation
        port(1234);
        // calling get will make your app start listening for the GET path with the /hello endpoint
        get("/hello", (req, res) -> "Hello World");

        get("/newuser", (req, res) -> {
            BasicDBObject newuser = new BasicDBObject();
            String[] friendsList = new String[0];
            newuser.put("username", req.queryMap().get("username").value());
            newuser.put("password", req.queryMap().get("password").value());
            newuser.put("friends", friendsList);
            colusers.insert(newuser);
            return "okay";
        });

        ///login?username=<username>&password=<wrongpassword>
        // AND /login?username=<username>&password=<pass>
        get("/login", (req, res) -> {
            BasicDBObject userlogin = new BasicDBObject();
            userlogin.put("username", req.queryMap().get("username").value());
            userlogin.put("password", req.queryMap().get("password").value());

            DBCursor cursor = colusers.find(userlogin);
            if (cursor.hasNext()) {
                String jonothansToken = "tokentoken";     //needs to be generated
                BasicDBObject myToken = new BasicDBObject();
                myToken.put("token", jonothansToken);
                myToken.put("timestamp", System.currentTimeMillis());
                myToken.put("username", req.queryMap().get("username").value());
                colauth.insert(myToken);
                return jonothansToken;    //needs to return token
            } else {
                return "login_failed";
            }
        });

        // /addfriend?token=<badtoken>&friend=<freindsuserid>
        get("/addfriend", (req, res) -> {
            BasicDBObject checkToken = new BasicDBObject();
            checkToken.put("token", req.queryMap().get("token").value());

            DBCursor cursor = colauth.find(checkToken);
            if (cursor.hasNext()) {
                String currentUsername = cursor.next().get("username").toString();

                BasicDBObject user = new BasicDBObject();
                user.put("username", currentUsername);

                DBCursor cursor1 = colusers.find(user);
                if (cursor1.hasNext()) {
                    System.out.println("cursor1");
                    BasicDBObject newFriend = new BasicDBObject();
                    newFriend.put("username", req.queryMap().get("friend").value());
                    BasicDBObject listItem = new BasicDBObject("friends", newFriend);
                    BasicDBObject updateQuery = new BasicDBObject("$push", listItem);
                    colusers.update(user, updateQuery);
                }
                return "okay";
            } else {
                return "failed_authentication";
            }
        });

        // /friends?token=<token>
        get("friends", (req, res) -> {
            BasicDBObject checkToken = new BasicDBObject();
            checkToken.put("token", req.queryMap().get("token").value());

            DBCursor cursor = colauth.find(checkToken);
            if (cursor.hasNext()) {
                //access token.username, use username to lookup friendslist of username in colusers
                DBObject dbo = colauth.findOne();
                String username = (String)dbo.get(req.queryMap().get("token").value());
                dbo = colusers.findOne();
                return dbo.get(username);
            }

            return " list of friends";
        });

    }
}

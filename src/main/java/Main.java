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

        ///newuser?username=<username>&password=<pass> HANDLED
        // AND /newuser?username=<anotheruser>&password=<pass>
        get("/newuser", (req, res) -> {
            BasicDBObject newuser = new BasicDBObject();
            String[] friendsList = new String[0];
            newuser.put("username", req.queryMap().get("username").value());
            newuser.put("password", req.queryMap().get("password").value());
            newuser.put("friends", friendsList);
            colusers.insert(newuser);                                               //put newuser into collection of users
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
                return "login_accepted";    //needs to return token
            } else {
                return "login_failed";
            }

        });

        // /addfriend?token=<badtoken>&friend=<freindsuserid>
        get("/addfriend", (req, res) -> {
            //req.queryMap().get("token").value();
            //req.queryMap().get("friend").value();

//            BasicDBObject newFriend = new BasicDBObject();
//            newFriend.put("username", req.queryMap().get("friend").value());
//
//            DBObject listItem = new BasicDBObject("friends", new BasicDBObject(newFriend));
//            DBObject updateQuery = new BasicDBObject("$push", listItem);
//            colusers.insert(updateQuery);

            return "failed_authentication";
        });

        // /friends?token=<token>
        get("friends", (req, res) -> {
            //req.queryMap().get("token").value();
            return " list of friends";
        });

    }
}

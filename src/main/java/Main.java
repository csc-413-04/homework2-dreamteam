package main.java;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

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
            BasicDBObject newuser = new BasicDBObject();                            //build newuser to put into collection of users
            newuser.put("username", req.queryMap().get("username").value());
            newuser.put("password", req.queryMap().get("password").value());
            colusers.insert(newuser);                                               //put newuser into collection of users
            return "okay";
        });

        ///login?username=<username>&password=<wrongpassword>
        // AND /login?username=<username>&password=<pass>
        get("/login", (req, res) -> {
            //req.queryMap().get("username").value();
            //req.queryMap().get("password").value();
            //build basicdbobject with username/password, look up in colusers, if match return login OK, if not login_failed
            //colusers.find(); maybe
            return "login_failed";
        });

        // /addfriend?token=<badtoken>&friend=<freindsuserid>
        get("/addfriend", (req, res) -> {
            //req.queryMap().get("token").value();
            //req.queryMap().get("friend").value();
            return "failed_authentication";
        });

        // /friends?token=<token>
        get("friends", (req, res) -> {
            //req.queryMap().get("token").value();
            return " list of friends";
        });

    }
}

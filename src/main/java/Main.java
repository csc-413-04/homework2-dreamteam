package main.java;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        // staticFiles.externalLocation("public");
        // http://sparkjava.com/documentation
        port(1234);
        // calling get will make your app start listening for the GET path with the /hello endpoint
        get("/hello", (req, res) -> "Hello World");

        ///newuser?username=<username>&password=<pass>
        // AND /newuser?username=<anotheruser>&password=<pass>
        get("/newuser", (req, res) -> {
            return req.queryMap().get("username").value() + req.queryMap().get("password").value();
        });

        ///login?username=<username>&password=<wrongpassword>
        // AND /login?username=<username>&password=<pass>
        get("/login", (req, res) -> {
            //req.queryMap().get("username").value();
            //req.queryMap().get("password").value();
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

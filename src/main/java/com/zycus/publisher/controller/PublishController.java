package com.zycus.publisher.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RestController
public class PublishController {

    public static long timeStamp = 0;

    @RequestMapping(value = "/publish/channel", method = RequestMethod.GET, produces = "text/plain")
    public String publishChannel(@RequestParam("message") String message, @RequestParam("channelName") String channelName) {
        Jedis jedis = new Jedis("localhost");
        System.out.println("Connection to server successful !");
        System.out.println("DONE : " + jedis.ping());

        jedis.publish(channelName, message);

        return " Message published on channel : " + channelName;
    }


    @RequestMapping(value = "/listenMessage", method = RequestMethod.GET, produces = "text/plain")
    public String hello() {
        Jedis jedis = new Jedis("localhost");
        System.out.println("Connection to server successful !");
        System.out.println("DONE : " + jedis.ping());

        long currentTimestamp = new Date().getTime();

        Set<String> messages = jedis.zrangeByScore("timeSeries", timeStamp, currentTimestamp);

        timeStamp = currentTimestamp;
        for (String message : messages) {
            System.out.println("Received message : " + message);
        }
        return "done";

    }

}

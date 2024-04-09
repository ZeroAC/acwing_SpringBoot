package com.kob.backend.controller.pk;

import com.kob.backend.consumer.WebSocketServer;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author zeroac
 *
 * 控制启动一局游戏的开始
 */
@RestController
@RequestMapping("/pk")
public class GameController {

    @RequestMapping("/startGame")
    public String startGame(@RequestParam MultiValueMap<String, String> data) {
        Integer aId = Integer.parseInt(Objects.requireNonNull(data.getFirst("a_id")));
        Integer bId = Integer.parseInt(Objects.requireNonNull(data.getFirst("b_id")));
        WebSocketServer.startGame(aId, bId);//静态函数 相当于把类当全局变量来用了，直接调用就好
        return "start game success";
    }

}

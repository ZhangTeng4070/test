package com.example.eventbus;

import com.google.common.eventbus.Subscribe;
//订阅者
public class MultiEventListener {

    @Subscribe
    public void listen(OrderEvent event){
        System.out.println("MultiEventListener receive msg: "+event.getMessage());
    }

}

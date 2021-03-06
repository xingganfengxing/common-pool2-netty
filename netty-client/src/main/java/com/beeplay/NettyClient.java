package com.beeplay;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class NettyClient {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8000;

    public static void main(String[] args) throws Exception {

            System.out.println("请输入需要发送的消息按回车发送  输入 quit 退出");

            GenericObjectPoolConfig config=new GenericObjectPoolConfig();

            config.setMaxIdle(10);//最大活跃数
            config.setMinIdle(1);//最小活跃数
            config.setMaxTotal(100);//最大总数

            ChannelPool channelPool=new ChannelPool(config,HOST,PORT);

            BufferedReader in = new BufferedReader(new InputStreamReader((System.in)));
            for (;;) {
                NettyChannel nettyChannel=channelPool.getResource();
                final String input = in.readLine();
                final String line = input != null? input.trim() : null;
                if (line == null || "quit".equalsIgnoreCase(line)) {
                    channelPool.returnResource(nettyChannel);
                    break;
                }else if (line.isEmpty()) {
                    continue;
                }
                nettyChannel.getCh().writeAndFlush(line);
                channelPool.returnResource(nettyChannel);
            }
    }
}

package org.example.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.util.concurrent.TimeUnit;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-04-11
 */
public class HelloClient {

    public static void main(String[] args) throws Exception {
        // 客户端引导器
        Bootstrap bootstrap = new Bootstrap();
        // 配置线程组
        bootstrap.group(new NioEventLoopGroup());
        // 指定 IO 类型为 NIO
        bootstrap.channel(NioSocketChannel.class);
        // 配置 IO 处理器
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new StringEncoder());
            }
        });
        // 建立连接
        Channel channel = bootstrap.connect("127.0.0.1",8081).channel();
        // 发送消息
        while (true) {
            channel.writeAndFlush("hello world..");
            TimeUnit.SECONDS.sleep(5);
        }
    }
}

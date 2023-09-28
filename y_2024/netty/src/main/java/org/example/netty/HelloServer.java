package org.example.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

import java.util.Date;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-04-11
 * https://www.skjava.com/series/article/1122976839
 */
public class HelloServer {

    public static void main(String[] args) throws InterruptedException {
        // 创建服务端启动引导器
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 配置线程池模型 EventLoopGroup 线程组；如果只配置一个，说明共用
        bootstrap.group(new NioEventLoopGroup());
        // 指定服务端的IO模型 Channel类型
        bootstrap.channel(NioServerSocketChannel.class);
        // 配置 option 参数
        bootstrap.option(ChannelOption.SO_REUSEADDR, false);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        // 定义处理器 Handler
        bootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                // 解码
                ch.pipeline().addLast(new StringDecoder());

                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        System.out.println(ctx.channel() + ", channel active!");
                    }

                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        System.out.println(new Date() + "：" + msg);
                    }
                });
            }
        });
        // 绑定端口
        ChannelFuture channelFuture = bootstrap.bind(8081).sync();

        ChannelFuture closeFuture = channelFuture.channel().closeFuture();
        // 阻塞，直到channel关闭
        closeFuture.sync();
    }
}

package org.example.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-04-12
 * https://www.skjava.com/series/article/1323683324
 */
public class ZnkfRgClient {

    private static class RgHandler extends SimpleChannelInboundHandler {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            String[] message = ((String) msg).split("&");
            String messageType = message[0];
            String[] values = message[1].split("\\|");
            String userChannelId = values[0];

            String rgChannelId = ctx.channel().id().toString();

            if ("USER_HELLO".equals(messageType)) {
                System.out.println("有用户[" + userChannelId + "]在等待人工坐席，请尽快接入");

                ctx.channel().writeAndFlush("RG_USER_MESSAGE" + "&" + userChannelId + "|您好，我是工号[" + rgChannelId + "]，请问您有什么问题需要咨询吗？");
            } else if ("RG_USER_MESSAGE".equals(messageType)) {
                String content = values[1];

                System.out.println("用户说：" + content);
                System.out.println();

                Scanner scanner = new Scanner(System.in);
                String rgContent = scanner.nextLine();

                // 人工坐席 Channel
                String toChannelId = values[0];
                rgContent = "RG_USER_MESSAGE" + "&" + toChannelId + "|" + rgContent;

                ctx.channel().writeAndFlush(rgContent);
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup());
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new StringDecoder());
                ch.pipeline().addLast(new StringEncoder());

                ch.pipeline().addLast(new RgHandler());
                System.out.println("channel init!");
            }
        });
        Channel channel = bootstrap.connect("127.0.0.1", 8081).sync().channel();

    }
}

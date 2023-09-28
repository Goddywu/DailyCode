package org.example.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.*;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-04-12
 */
public class ZnkfServer {

    private static class ChannelSessionMemory {

        private static final Map<String, Channel> channelMap = new HashMap<>();
        private static final List<String> waitingUserChannelIds = new ArrayList<>();
        private static final List<String> rgUserChannelIds = new ArrayList<>();

        public static void putClientChannel(String channelId, Channel channel) {
            channelMap.put(channelId, channel);
        }

        public static String getRgChannelId() {
            return rgUserChannelIds.remove(0);
        }

        public static void putWaitingUser(String channelId) {
            waitingUserChannelIds.add(channelId);
        }

        public static Channel getChannel(String channelId) {
            return channelMap.get(channelId);
        }

        public static String getWaitingUser() {
            return waitingUserChannelIds.remove(0);
        }

        public static void putRg(String channelId) {
            rgUserChannelIds.add(channelId);
        }
    }

    private static class UserRgChatHandler extends SimpleChannelInboundHandler {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            String[] messages = ((String)msg).split("&");
            String messageType = messages[0];
            String value = messages[1];

            if ("RG_USER_MESSAGE".equals(messageType)) {
                String[] values = messages[1].split("\\|");
                // 转发的内容
                String content = values[1];

                // 发给谁
                String toChannelId = values[0];
                Channel toChannel = ChannelSessionMemory.getChannel(toChannelId);

                // 从哪里来
                String fromChannelId = ctx.channel().id().toString();

                // message
                content = "RG_USER_MESSAGE" + "&" + fromChannelId + "|" + content;

                // 发送内容
                toChannel.writeAndFlush(content);
            } else {
                ctx.fireChannelRead(msg);
            }
        }
    }

    private static class RgRegisterHandler extends SimpleChannelInboundHandler {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            String[] messages = ((String) msg).split("&");
            String key = messages[0];
            String value = messages[1];

            if ("RG_REGISTER".equals(key)) {
                Channel channel = ctx.channel();
                String channelId = channel.id().toString();
                ChannelSessionMemory.putClientChannel(channelId, channel);

                String waitingUser = ChannelSessionMemory.getWaitingUser();
                if (waitingUser == null || waitingUser.isEmpty()) {
                    ChannelSessionMemory.putRg(channelId);
                } else {
                    channel.writeAndFlush("USER_HELLO" + "&" + waitingUser + "|-1");
                }
            } else {
                ctx.fireChannelRead(msg);
            }
        }
    }

    private static class ClientRegisterHandler extends SimpleChannelInboundHandler {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            String[] messages = ((String) msg).split("&");

            // 消息体有两部分构成  key&value
            // key 用来区分身份，value 是真正的消息
            String key = messages[0];
            String value = messages[1];

            Channel channel = ctx.channel();

            if ("USER_REGISTER".equals(key)) {
                // 用户
                ChannelSessionMemory.putClientChannel(channel.id().toString(), channel);

                // 用户注册成功后，立刻向用户发送问题列表
                sendInitMessage(channel);
            } else {
                ctx.fireChannelRead(value);
            }
        }

        private void sendInitMessage(Channel channel) {
            StringBuilder message = new StringBuilder(channel.id().toString() + ",您好，我是智能客服。请告诉你想咨询的服务").append("\r\n")
                    .append("1. 询问天气").append("\r\n")
                    .append("0. 人工").append("\r\n")
                    .append("==================请输入咨询问题的编号==================");
            channel.writeAndFlush(message);
        }
    }

    private static class AiAnswerHandler extends SimpleChannelInboundHandler {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            Channel channel = ctx.channel();
            String userChannelId = channel.id().toString();
            String[] messages = ((String) msg).split("&");
            String key = messages[0];
            String value = messages[1];

            if ("USER_QUESTION".equals(key)) {

                if ("0".equals(value)) {
                    String rgChannelId = ChannelSessionMemory.getRgChannelId();
                    if (rgChannelId == null) {
                        ChannelSessionMemory.putWaitingUser(userChannelId);
                        ctx.channel().writeAndFlush("暂无人工坐席");
                    } else {
                        Channel rgChannel = ChannelSessionMemory.getChannel(rgChannelId);
                        rgChannel.writeAndFlush("USER_HELLO" + "&" + userChannelId + "|有用户咨询");
                    }

                } else {
                    String message = "天气晴朗";
                    ctx.channel().writeAndFlush(message);
                }
            }

        }
    }

    public static void main(String[] args) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup());
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline().addLast(new StringDecoder());
                ch.pipeline().addLast(new StringEncoder());

                ch.pipeline().addLast(new ClientRegisterHandler());
                ch.pipeline().addLast(new AiAnswerHandler());
                ch.pipeline().addLast(new RgRegisterHandler());
                ch.pipeline().addLast(new UserRgChatHandler());
                System.out.println("channel init!");
            }
        });
        bootstrap.bind(8081);
    }
}

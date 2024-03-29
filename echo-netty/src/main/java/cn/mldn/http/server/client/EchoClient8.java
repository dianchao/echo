package cn.mldn.http.server.client;

import cn.mldn.http.server.client.handler.EchoClientHandler7;
import cn.mldn.http.server.client.handler.EchoClientHandler8;
import cn.mldn.http.server.serious.MarshallingCodeFactory;
import cn.mldn.http.server.serious.MessagePackDecoder;
import cn.mldn.http.server.serious.MessagePackEncoder;
import cn.mldn.info.HostInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class EchoClient8 {
    public void run() throws Exception {
        // 1、如果现在客户端不同，那么也可以不使用多线程模式来处理;
        // 在Netty中考虑到代码的统一性，也允许你在客户端设置线程池
        EventLoopGroup group = new NioEventLoopGroup(); // 创建一个线程池
        try {
            Bootstrap client = new Bootstrap(); // 创建客户端处理程序
            client.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true) // 允许接收大块的返回数据
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(MarshallingCodeFactory.builderMarshallingEncoder());
                            socketChannel.pipeline().addLast(MarshallingCodeFactory.builderMarshallingDecoder());
                            socketChannel.pipeline().addLast(new EchoClientHandler8()); // 追加了处理器
                        }
                    });
            ChannelFuture channelFuture = client.connect(HostInfo.HOST_NAME, HostInfo.PORT).sync();
            channelFuture.addListener(new GenericFutureListener() {
                @Override
                public void operationComplete(Future future) throws Exception {
                    if(future.isSuccess()){
                        System.out.println("服务器连接已经完成，可以确保进行消息的准确传输。");
                    }
                }
            });
            channelFuture.channel().closeFuture().sync(); // 关闭连接

        } finally {
            group.shutdownGracefully();
        }
    }
}

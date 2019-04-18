package cn.mldn.http.server.client.handler;

import cn.mldn.info.HostInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * 使用自定义分隔符
 */
public class EchoClientHandler5 extends ChannelInboundHandlerAdapter {
    private static final int REPEAT = 500;// 消息重复发送次数

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for(int i = 0; i < REPEAT; i++){ //重复发送消息
            ctx.writeAndFlush("[" + i+ "] Hello World" + HostInfo.SEPARATOR); //使用自定义分隔符
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 只要服务器端发送完成信息之后，都会执行此方法进行内容的输出操作
        try {
            String readData = msg.toString().trim();//接收返回数据内容
            System.out.println(readData); //输出服务器端的响应内容
        } finally {
            ReferenceCountUtil.release(msg); // 释放缓存
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

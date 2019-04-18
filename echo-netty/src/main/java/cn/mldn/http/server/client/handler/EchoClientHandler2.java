package cn.mldn.http.server.client.handler;

import cn.mldn.util.InputUtil;
import cn.mldn.vo.Member;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * 演示粘包和拆包的问题
 */
public class EchoClientHandler2 extends ChannelInboundHandlerAdapter {
    private static final int REPEAT = 500;// 消息重复发送次数

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for(int i = 0; i < REPEAT; i++){ //重复发送消息
            byte[] data = ("[" + i+ "] Hello World").getBytes();
            ByteBuf sendBuf = Unpooled.buffer(data.length);
            sendBuf.writeBytes(data); //将数据保存到缓冲区
            ctx.writeAndFlush(sendBuf) ; //将缓冲区中的消息发送
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 只要服务器端发送完成信息之后，都会执行此方法进行内容的输出操作
        try {
            ByteBuf readBuf = (ByteBuf) msg;
            String readData = readBuf.toString(CharsetUtil.UTF_8);//接收返回数据内容
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

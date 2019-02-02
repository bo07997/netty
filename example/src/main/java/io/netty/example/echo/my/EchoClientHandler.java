package io.netty.example.echo.my;

//   1)、channelActive()--在服务器的连接已经建立之后将被调用
//   2)、channelRead0()--当从服务器接收到一条消息时被调用；
//   3)、exceptionCaught()--在处理过程中引发异常被调用

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf>{

    /**
     * 接收到数据时，调用该方法
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        System.out.println("Client received :"+in.toString(CharsetUtil.UTF_8)); //记录已经接收消息的转储

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当被知道Channel活跃时，发送一条消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks2!",CharsetUtil.UTF_8));
    }

    /**
     * 发生异常时，记录异常信息同时关闭Channel
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}

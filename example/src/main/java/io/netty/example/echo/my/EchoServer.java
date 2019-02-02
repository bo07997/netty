package io.netty.example.echo.my;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {
    private final int port;

    public EchoServer(int port){
        this.port = port;
    }

    public static void main(String[] args) throws Exception{
//        if(args.length!=1){
//            System.err.println("Usage:"+EchoServer.class.getSimpleName() + "");
//        }

        //设置端口，如果端口参数不正确，则抛出一个NumberFormatException的异常
        int port = 8023;
        new EchoServer(port).start();
    }

    public void start() throws Exception{
        final EchoServerHandler serverHandler = new EchoServerHandler();
        EventLoopGroup workGroup = new NioEventLoopGroup(); //创建EventLooproup
        EventLoopGroup bossGroup = new NioEventLoopGroup(); //创建EventLooproup
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(workGroup,bossGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer(){
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(serverHandler);//EchoServerHandler被标记赤@Shareable，所以我们可以同时使用同样的实你还
                        }

                    });

            //绑定端口，待待同步
            ChannelFuture f = b.bind(port).sync();  //异步绑定服务器，调用 sync()方法阻塞等待直到绑定完成
            //等待服务器监听，端口关闭
            f.channel().closeFuture().sync();       //sync会直到绑定操作结束为止。

            //b.localAddress(new InetSocketAddress(port));//使用指定的端口设置套装字地址

        }finally{
            workGroup.shutdownGracefully().sync();//关闭EventLoopGroup，释放所有的资源
            bossGroup.shutdownGracefully().sync();
        }

    }

}

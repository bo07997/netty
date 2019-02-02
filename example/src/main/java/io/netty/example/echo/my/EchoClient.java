package io.netty.example.echo.my;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {
    private final int port;
    private final String host;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
//        if (args.length != 2) {
//            System.err.println("参数不对 ：" + EchoClient.class.getSimpleName() + "");
//        }
        // 设置端口，如果端口参数不正确，则抛出一个NumberFormatException的异常

        String host = "127.0.0.1";
        int port = 8023;
        new EchoClient(host,port).start();

        //System.err.println("Start " + EchoClient.class.getSimpleName() + "");
    }

    public void start() throws Exception {
        //final EchoServerHandler serverHandler = new EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();     // 创建EventLooproup
        //EventLoopGroup bossgroup = new NioEventLoopGroup(); //创建EventLooproup
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)// 指定EventLoopGropu以处理客户端事件，需要适用于NIO的实现
                .channel(NioSocketChannel.class)
                //  b.option(ChannelOption.SO_BACKLOG, 1024);
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new EchoClientHandler());
                    }

                });

            // 绑定端口，待待同步
            ChannelFuture f = b.connect(host, port).sync(); // 异步绑定服务器，调用
            // sync()方法阻塞等待直到绑定完成
            // 等待服务器监听，端口关闭
            f.channel().closeFuture().sync(); // sync会直到绑定操作结束为止。

        } finally {
            group.shutdownGracefully().sync();// 关闭EventLoopGroup，释放所有的资源
        }

    }
}

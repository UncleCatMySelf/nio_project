package com.github.myself;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * Created by MySelf on 2018/12/25.
 */
public class NioServer {

    public void server(int port) throws IOException {
        //1、打开服务器套接字通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //2、设定为非阻塞、调整此通道的阻塞模式。
        serverSocketChannel.configureBlocking(false);
        //3、检索与此通道关联的服务器套接字。
        ServerSocket serverSocket = serverSocketChannel.socket();
        //4、此类实现 ip 套接字地址 (ip 地址 + 端口号)
        InetSocketAddress address = new InetSocketAddress(port);
        //5、将服务器绑定到选定的套接字地址
        serverSocket.bind(address);
        //6、打开Selector来处理Channel
        Selector selector = Selector.open();
        //7、将ServerSocket注册到Selector已接受连接，注册会判断是否为非阻塞模式
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

    }

}

package com.github.myself.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by MySelf on 2018/12/24.
 */
public class NioServer {

    public void server(int port) throws IOException{
        //可选择的通道, 用于面向流的侦听插槽。
        //open:打开服务器套接字通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //设定为非阻塞
        serverSocketChannel.configureBlocking(false);
        //此类实现服务器套接字。服务器套接字等待 通过网络进入的请求。它根据该请求执行一些操作, 然后可能将结果返回给请求者。
        //socket：检索与此通道关联的服务器套接字。
        ServerSocket serverSocket = serverSocketChannel.socket();
        //此类实现 ip 套接字地址 (ip 地址 + 端口号) 它也可以是一对 (主机名 + 端口号), 在这种情况下, 将尝试解析主机名。如果解析失败, 则该地址被称为 <I> 未解决 </I> 但仍可在某些情况下使用, 例如通过代理连接。
        InetSocketAddress address = new InetSocketAddress(port);
        //将服务器绑定到选定的端口
        serverSocket.bind(address);
        //打开Selector来处理Channel
        Selector selector = Selector.open();
        //将ServerSocket注册到Selector已接受连接
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer readBuff = ByteBuffer.allocate(1024);
        final ByteBuffer msg = ByteBuffer.wrap("Hi!\r\n".getBytes());
//        msg.flip();
        while (true){
            try {
                //等到需要处理的新事件：阻塞将一直持续到下一个传入事件
                selector.select();
            }catch (IOException e){
                e.printStackTrace();
                //handle exception
                break;
            }
            //获取所有接收事件的SelectionKey实例
            Set<SelectionKey> readykeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readykeys.iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                try {
                    //检查事件是否是一个新的已经就绪可以被接受的连接
                    if (key.isAcceptable()){
                        ServerSocketChannel server = (ServerSocketChannel)key.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        //接受客户端，并将它注册到选择器
                        client.register(selector,SelectionKey.OP_WRITE | SelectionKey.OP_READ,msg.duplicate());
                        System.out.println("Accepted connection from " + client);
                    }
                    if (key.isReadable()){
                        SocketChannel socketChannel = (SocketChannel)key.channel();
                        readBuff.clear();
                        socketChannel.read(readBuff);
                        readBuff.flip();
                        System.out.println("received:"+new String(readBuff.array()));
                        key.interestOps(SelectionKey.OP_WRITE);
                    }
                    //检查套接字是否已经准备好写数据
                    if (key.isWritable()){
                        SocketChannel client = (SocketChannel)key.channel();
                        ByteBuffer buffer = (ByteBuffer)key.attachment();
                        buffer.rewind();
                        client.write(buffer);
                        key.interestOps(SelectionKey.OP_READ);
                    }
                }catch (IOException e){
                   e.printStackTrace();
                }
            }
        }
    }

}

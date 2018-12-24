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
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
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

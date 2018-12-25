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
        //通过调用此类的 {@link #open () open} 方法创建服务器套接字通道。 不可能为任意的、预先存在的 {@link serversocket} 创建通道。新创建的服务器套接字通道已打开, 但尚未绑定。 尝试调用未绑定的服务器套接字通道的 {@link #accept (接受}) 方法将导致引发 {@link notyet-bvexception}。可以通过调用此类定义的 {@link #bind (java.net.SocketAddress, int) 绑定} 方法之一来绑定服务器套接字通道。
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //设定为非阻塞、调整此通道的阻止模式。
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
        //将ServerSocket注册到Selector已接受连接，注册会判断是否为非阻塞模式
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
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
                //表示使用 {@link 选择器} 的 {@link 选择通道} 的注册的标记。
                SelectionKey key = iterator.next();
                iterator.remove();
                try {
                    //检查事件是否是一个新的已经就绪可以被接受的连接
                    if (key.isAcceptable()){
                        //channel：返回为其创建此键的通道。 即使在取消密钥后, 此方法仍将继续返回通道。
                        ServerSocketChannel server = (ServerSocketChannel)key.channel();
                        //套接字通道是通过调用此类的 {@link #open 开放} 方法之一来创建的。 不可能为任意的、预先存在的套接字创建通道。新创建的套接字通道已打开, 但尚未连接。 尝试在未连接的通道上调用 ito 操作将导致引发 {@link。 套接字通道可以通过调用其 {@link #connect 连接} 方法进行连接;一旦连接, 套接字通道将保持连接, 直到关闭。 是否连接套接字通道可以通过调用其 {@link #isConnected 已连接} 方法来确定。
                        //可选择的通道, 用于面向流的连接插槽。
                        //accept：接受与此通道的套接字所建立的连接。
                        SocketChannel client = server.accept();
                        System.out.println("是否连接"+client.isConnected());
                        //设定为非阻塞
                        client.configureBlocking(false);
                        //接受客户端，并将它注册到选择器，并添加附件
                        client.register(selector,SelectionKey.OP_WRITE | SelectionKey.OP_READ,msg.duplicate());
                        System.out.println("Accepted connection from " + client);
                    }
                    //检查套接字是否已经准备好读数据
                    if (key.isReadable()){
                        SocketChannel client = (SocketChannel)key.channel();
                        readBuff.clear();
                        client.read(readBuff);
                        readBuff.flip();
                        System.out.println("received:"+new String(readBuff.array()));
                        //将此键的兴趣集设置为给定的值。 OP_WRITE
                        key.interestOps(SelectionKey.OP_WRITE);
                    }
                    //检查套接字是否已经准备好写数据
                    if (key.isWritable()){
                        SocketChannel client = (SocketChannel)key.channel();
                        //attachment : 检索当前附件
                        ByteBuffer buffer = (ByteBuffer)key.attachment();
                        buffer.rewind();
                        client.write(buffer);
                        //将此键的兴趣集设置为给定的值。 OP_READ
                        key.interestOps(SelectionKey.OP_READ);
                    }
                }catch (IOException e){
                   e.printStackTrace();
                }
            }
        }
    }

}

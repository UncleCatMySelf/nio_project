package com.github.myself;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by MySelf on 2018/12/17.
 */
public class WebServer {

    public static void main(String[] args) {
        try {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.socket().bind(new InetSocketAddress("127.0.0.1",8900));
            //设定为非阻塞
            ssc.configureBlocking(false);

            Selector selector = Selector.open();
            //注册channel，并且指定感兴趣的事件是Accept(接收)
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            ByteBuffer readBuff = ByteBuffer.allocate(1024);
            ByteBuffer writeBuff = ByteBuffer.allocate(128);
            writeBuff.put("received".getBytes());
            writeBuff.flip();
            int a = 1;

            while (true){
                int nReady = selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();

                while (it.hasNext()){
                    a++;
                    SelectionKey key = it.next();
                    it.remove();
                    System.out.println("----");
                    if (key.isAcceptable()) {
                        //创建新的连接，并且把链接注册到selector上
                        //声明这个channel只对读操作感兴趣
                        SocketChannel socketChannel = ssc.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("Acceptable");
                    }else if (key.isConnectable()){
                        System.out.println("Connect");
                    }else if(key.isReadable()){
                        SocketChannel socketChannel = (SocketChannel)key.channel();
                        readBuff.clear();
                        socketChannel.read(readBuff);
                        readBuff.flip();
                        System.out.println("received:"+new String(readBuff.array()));
                        key.interestOps(SelectionKey.OP_WRITE);
                        System.out.println("Readable");
                    }else if(key.isWritable()){
                        writeBuff.rewind();
                        SocketChannel socketChannel = (SocketChannel)key.channel();
                        socketChannel.write(writeBuff);
                        key.interestOps(SelectionKey.OP_READ);
                        System.out.println("Writable");
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

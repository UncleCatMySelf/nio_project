package com.github.myself.channel;

import java.io.IOException;

/**
 * Created by MySelf on 2018/12/24.
 */
public class Application {

    public static void main(String[] args) throws IOException {
        NioServer nioServer = new NioServer();
        nioServer.server(8090);

//        OioServer oioServer = new OioServer();
//        oioServer.server(8090);

    }

}

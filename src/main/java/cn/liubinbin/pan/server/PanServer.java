/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package cn.liubinbin.pan.server;

import cn.liubinbin.pan.bcache.BcacheManager;
import cn.liubinbin.pan.conf.Config;
import cn.liubinbin.pan.jmx.Jmx;
import cn.liubinbin.pan.metrics.Metrics;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author liubinbin
 */
public final class PanServer {

    public static void main(String[] args) throws Exception {
        Config cacheConfig = new Config();

        // metrics
        Metrics metrics = new Metrics(cacheConfig);
        metrics.start();

        // jmx
        Jmx jmx = new Jmx(cacheConfig, metrics.getServerLoad());
        jmx.start();

        BcacheManager cacheManager = new BcacheManager(cacheConfig, metrics);
        byte[] CONTENT = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] CONTENT1 = {'j', 'a', 'v', 'a', 'i', 's', 'g', 'r', 'e', 'a', 't'};
        byte[] CONTENT2 = new byte[73060];
        byte[] CONTENT3 = new byte[247917];

        CONTENT2[73060 - 1] = '1';
        cacheManager.put("key".getBytes(), CONTENT);
        cacheManager.put("key1".getBytes(), CONTENT1);
        cacheManager.put("key2".getBytes(), CONTENT2);
        cacheManager.put("key5".getBytes(), CONTENT3);

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(cacheConfig.getNettyThreadCount());
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.ERROR)).childHandler(new PanServerInitializer(cacheManager));

            Channel ch = b.bind(cacheConfig.getPort()).sync().channel();

            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

/*
 * Copyright 2013 The Netty Project
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
package main.java.cn.liubinbin.pan.server.cache;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.AsciiString;
import io.netty.util.CharsetUtil;
import main.java.cn.liubinbin.pan.conf.Contants;
import main.java.cn.liubinbin.pan.experiment.cache.CacheManager;
import main.java.cn.liubinbin.pan.experiment.cache.Key;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/**
 * @author liubinbin
 * 
 */

public class CacheServerHandler extends ChannelInboundHandlerAdapter {

//	private static final Logger logger = LogManager.getLogger(CacheServerHandler.class);
	private CacheManager cacheManager;

	public CacheServerHandler(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	private static final byte[] CONTENT = { 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd' };
	private static final byte[] CONTENT1 = { 'j', 'a', 'v', 'a', 'i', 's', 'g', 'r', 'e', 'a', 't' };

	private static final AsciiString CONTENT_TYPE = AsciiString.cached("Content-Type");
	private static final AsciiString CONTENT_LENGTH = AsciiString.cached("Content-Length");
	private static final AsciiString CONNECTION = AsciiString.cached("Connection");
	private static final AsciiString KEEP_ALIVE = AsciiString.cached("keep-alive");

	private CompositeByteBuf tempData = null;
	private byte[] key = null;
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if (msg instanceof HttpRequest) {
			HttpRequest req = (HttpRequest) msg;

			boolean keepAlive = HttpUtil.isKeepAlive(req);
			
			if (req.method().equals(HttpMethod.GET)) {
				//deal with get request
				final String uri = req.uri();
				final String path = sanitizeUri(uri);
				if (path == null) {
					sendError(ctx, FORBIDDEN);
					return;
				}
				byte[] key = path.getBytes();
				ByteBuf value = cacheManager.getByByteBuf(key);

				FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, value);
				response.headers().set(CONTENT_TYPE, "text/plain");
				response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
				if (!keepAlive) {
					ctx.write(response).addListener(ChannelFutureListener.CLOSE);
				} else {
					response.headers().set(CONNECTION, KEEP_ALIVE);
					ctx.write(response);
				}
			} else if (req.method().equals(HttpMethod.PUT)) {
				//deal with put request
//				if (msg instanceof HttpContent) {
//					System.out.println("liubb HttpContent");
//					if (msg instanceof LastHttpContent) {
//						System.out.println("liubb HttpContent");
//					}
//				}
				final String uri = req.uri();
				final String path = sanitizeUri(uri);
				if (path == null) {
					sendError(ctx, FORBIDDEN);
					return;
				}
				key = path.getBytes();
				
				if (req instanceof HttpRequest){
					tempData = Unpooled.compositeBuffer();
					System.out.println("liubb HttpRequest");
					if (req instanceof FullHttpRequest) {
						System.out.println("liubb FullHttpRequest");
					} else {
						System.out.println("liubb not FullHttpRequest");
					}
				} else {
					System.out.println("liubb not HttpRequest" + req.getClass());
				}
//				System.out.println("abc");
			}

		} else if (msg instanceof HttpContent) {
			ByteBuf abc = ((HttpContent) msg).content();
			tempData.addComponent(true, abc.duplicate());
			System.out.println("liubb HttpContent " + abc.readableBytes() + " " + tempData.numComponents() + " " + tempData.maxNumComponents() + " " + tempData.maxCapacity() + " " + tempData.maxWritableBytes());
			if (msg instanceof LastHttpContent) {
				int length = tempData.readableBytes();
				byte[] data = new byte[length];
				tempData.getBytes(tempData.readerIndex(), data);
				System.out.println("key.length: " + key.length + " data.length: " + data.length);
				cacheManager.put(key, data);
				System.out.println("liubb LastHttpContent " + length + " " + data.length);
			}
		} 
	}

	private static String sanitizeUri(String uri) {
		// Decode the path.
		try {
			uri = URLDecoder.decode(uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new Error(e);
		}

		return uri.split(Contants.FILE_SEPARATOR)[1];
	}

	private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status,
				Unpooled.copiedBuffer("Failure: " + status + "\r\n", CharsetUtil.UTF_8));
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");

		// Close the connection as soon as the error message is sent.
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}

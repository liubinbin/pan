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
package main.java.cn.liubinbin.pan.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
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
import main.java.cn.liubinbin.pan.manager.CacheManager;

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

public class PanServerHandler extends ChannelInboundHandlerAdapter {

//	private static final Logger logger = LogManager.getLogger(CacheServerHandler.class);
	private CacheManager cacheManager;

	public PanServerHandler(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
		this.tempData = Unpooled.compositeBuffer();
	}

	private static final AsciiString CONTENT_TYPE = AsciiString.cached("Content-Type");
	private static final AsciiString CONTENT_LENGTH = AsciiString.cached("Content-Length");
	private static final AsciiString CONNECTION = AsciiString.cached("Connection");
	private static final AsciiString KEEP_ALIVE = AsciiString.cached("keep-alive");

	private CompositeByteBuf tempData = null;
	private byte[] key = null;
	private boolean isGet = true;
	private HttpRequest HttpRequest = null;
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if (msg instanceof HttpRequest) {
			HttpRequest req = (HttpRequest) msg;

			boolean keepAlive = HttpUtil.isKeepAlive(req);
			
			tempData.clear();
			key = null;
			
			if (req.method().equals(HttpMethod.GET)) {
				isGet = true;
				//deal with get request
				final String uri = req.uri();
				final String path = sanitizeUri(uri);
				if (path == null) {
					sendError(ctx, FORBIDDEN);
					return;
				}
				byte[] key = path.getBytes();
				ByteBuf value = cacheManager.getByByteBuf(key);
				if (value == null) {
					FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND);
					response.headers().set(CONTENT_TYPE, "text/plain");
					if (!keepAlive) {
						ctx.write(response).addListener(ChannelFutureListener.CLOSE);
					} else {
						response.headers().set(CONNECTION, KEEP_ALIVE);
						ctx.write(response);
					}
				} else {
					FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, value);
					response.headers().set(CONTENT_TYPE, "text/plain");
					response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
					if (!keepAlive) {
						ctx.write(response).addListener(ChannelFutureListener.CLOSE);
					} else {
						response.headers().set(CONNECTION, KEEP_ALIVE);
						ctx.write(response);
					}
				}

			} else if (req.method().equals(HttpMethod.PUT)) {
				//deal with put request
				isGet = false;
				final String uri = req.uri();
				final String path = sanitizeUri(uri);
				if (path == null) {
					sendError(ctx, FORBIDDEN);
					return;
				}
				key = path.getBytes();
				
				if (req instanceof HttpRequest){
					tempData.clear();
					System.out.println("It is HttpRequest");
					if (req instanceof FullHttpRequest) {
						System.out.println("It is FullHttpRequest");
					} else {
						System.out.println("It is not FullHttpRequest");
					}
				} else {
					System.out.println("It is not HttpRequest" + req.getClass());
				}
			} else if (req.method().equals(HttpMethod.DELETE)) {
				final String uri = req.uri();
				final String path = sanitizeUri(uri);
				if (path == null) {
					sendError(ctx, FORBIDDEN);
					return;
				}
				byte[] key = path.getBytes();
				cacheManager.delete(key);
				FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
				response.headers().set(CONTENT_TYPE, "text/plain");
				response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
				if (!keepAlive) {
					ctx.write(response).addListener(ChannelFutureListener.CLOSE);
				} else {
					response.headers().set(CONNECTION, KEEP_ALIVE);
					ctx.write(response);
				}
				System.out.println("we receive a delete request");
			}

		} else if (msg instanceof HttpContent) {
			if (! isGet) {
				ByteBuf tempContent = ((HttpContent) msg).content();
				if (tempContent.isReadable()) {
					tempData.addComponent(true, tempContent.duplicate());
//					System.out.println(ByteBufUtil.hexDump(tempContent));
					System.out.println("It is HttpContent " + tempContent.readableBytes() + " " + tempData.numComponents() + " " + tempData.maxNumComponents() + " " + tempData.maxCapacity() + " " + tempData.maxWritableBytes());
				} else {
					System.out.println("abc is not readable");
				}
				if (msg instanceof LastHttpContent) {
					int length = tempData.readableBytes();
					byte[] data = new byte[length];
					tempData.getBytes(tempData.readerIndex(), data);
					System.out.println("key.length: " + key.length + " data.length: " + data.length);
					cacheManager.put(key, data);
					System.out.println("It is LastHttpContent " + length + " " + data.length);
					FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
					response.headers().set(CONTENT_TYPE, "text/plain");
					if (!false) {
						ctx.write(response).addListener(ChannelFutureListener.CLOSE);
					} else {
						response.headers().set(CONNECTION, KEEP_ALIVE);
						ctx.write(response);
					}
				}
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

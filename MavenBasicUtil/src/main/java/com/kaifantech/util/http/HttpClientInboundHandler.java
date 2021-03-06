package com.kaifantech.util.http;

import com.kaifantech.util.log.AppFileLogger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;

public class HttpClientInboundHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof HttpResponse) {
			HttpResponse response = (HttpResponse) msg;
			HttpHeaders header = response.headers();
			AppFileLogger.warning("> CONTENT_TYPE:" + header.get(HttpHeaderNames.CONTENT_TYPE));
		}
		if (msg instanceof HttpContent) {
			HttpContent content = (HttpContent) msg;
			ByteBuf buf = content.content();
			AppFileLogger.warning("> CONTENT:");
			AppFileLogger.warning(buf.toString(io.netty.util.CharsetUtil.UTF_8));
			buf.release();
		}
	}
}
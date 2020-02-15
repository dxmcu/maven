package com.kaifantech.util.socket.netty.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class InitClientStrHandlerInitializer extends ChannelInitializer<SocketChannel> {
	private SimpleChannelInboundHandler<String> handler = null;

	public InitClientStrHandlerInitializer(SimpleChannelInboundHandler<String> handler) {
		this.handler = handler;
	}

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new StringEncoder());
		pipeline.addLast(new StringDecoder());
		pipeline.addLast(handler);
	}

}

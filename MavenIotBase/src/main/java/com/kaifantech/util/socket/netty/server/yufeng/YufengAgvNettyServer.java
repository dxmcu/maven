package com.kaifantech.util.socket.netty.server.yufeng;

import java.net.InetSocketAddress;

import org.springframework.scheduling.annotation.Async;

import com.calculatedfun.util.AppTool;
import com.kaifantech.bean.iot.client.IotClientBean;
import com.kaifantech.init.sys.params.AppSysParameters;
import com.kaifantech.util.socket.netty.server.DefauNettyByteServer;
import com.kaifantech.util.thread.ThreadTool;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

public class YufengAgvNettyServer extends DefauNettyByteServer {
	private EventLoopGroup group;
	private boolean isRunning = false;

	public EventLoopGroup getGroup() {
		return group;
	}

	public void setGroup(EventLoopGroup group) {
		this.group = group;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public YufengAgvNettyServer(IotClientBean iotClientBean) {
		super(iotClientBean);
	}

	public void closeResource() {
		try {
			group.shutdownGracefully().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Async
	private synchronized void loopInit() {
		while (true) {
			ThreadTool.sleep(1500);
			if (!AppSysParameters.isConnectIotServer()) {
				continue;
			}
			if (isRunning) {
				continue;
			}
			try {
				doInit();
			} catch (Exception e) {
				isRunning = false;
			}
		}
	}

	@Async
	public void keepAlive() {
		// while (true) {
		// ThreadTool.sleep(getHeartBeat().getHeartBeatInterval());
		// sendMsg(ByteUtil.hexStringToBytes(getHeartBeat().getHeartBeatStr()));
		// }
	}

	private void doInit() throws Exception {
		isRunning = true;
		final NettySocketServerHandler serverHandler = new NettySocketServerHandler();
		group = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(group).channel(NioServerSocketChannel.class)
					.localAddress(new InetSocketAddress(Integer.parseInt(getIotClientBean().getPort())))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(serverHandler);
						}
					});
			ChannelFuture f = b.bind().sync();
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully().sync();
			isRunning = false;
		}
	}

	public void init() throws Exception {
		ThreadTool.run(() -> {
			loopInit();
		});
	}

	@Sharable
	class NettySocketServerHandler extends ChannelInboundHandlerAdapter {

		public synchronized void dealData(ChannelHandlerContext ctx, ByteBuf in) {
			String s = in.toString(CharsetUtil.UTF_8);
			setMsg(s);
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) {
			ByteBuf in = (ByteBuf) msg;
			dealData(ctx, in);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
			cause.printStackTrace();
			ctx.close();
			isRunning = false;
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			doSendToClient(ctx);
			super.channelActive(ctx);
		}

		@Override
		public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
			ctx.close();
			isRunning = false;
		}

		public void doSendToClient(ChannelHandlerContext ctx) {
			ThreadTool.run(() -> {
				while (true) {
					if (!AppSysParameters.isConnectIotServer()) {
						ctx.close();
						isRunning = false;
						break;
					}
					try {
						String cmd = getCmd();
						if (!AppTool.isNull(cmd)) {
							ctx.writeAndFlush(Unpooled.copiedBuffer(cmd, CharsetUtil.UTF_8));
						}
						setCmd(null);
						ThreadTool.sleep(1000);
					} catch (Exception e) {
						closeResource();
						isRunning = false;
						try {
							init();
						} catch (Exception ee) {
							ee.printStackTrace();
						}
					}
				}
			});
		}

	}

	public static YufengAgvNettyServer create(IotClientBean iotClientBean) {
		return new YufengAgvNettyServer(iotClientBean);
	}
}
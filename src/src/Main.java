import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class Main {
	
	private int port;
	
	public Main(int port) throws Exception {
		this.port = port;
		this.launch();
	}
	
	public void launch() throws Exception {
		ServerBootstrap bootstrap = new ServerBootstrap();
		EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
		bootstrap.group(boss,work)
			.channel(NioServerSocketChannel.class)
			.childHandler(new MyChannelInitializer());
		ChannelFuture future = bootstrap.bind(port).sync();
		System.out.println("服务器已启动，端口号为:"+port);
		future.channel().closeFuture().sync();
	}
	
	class MyChannelInitializer extends ChannelInitializer<SocketChannel>{

		@Override
		protected void initChannel(SocketChannel ch) throws Exception {

			ChannelPipeline pipeline = ch.pipeline();
	        pipeline.addLast(new HttpServerCodec());// http 编解码
	        pipeline.addLast("httpAggregator",new HttpObjectAggregator(512*1024)); // http消息聚合器
			pipeline.addLast(new MyHandler());
		}

	}

	public static void main(String[] args) {
		
		try {
			new Main(9009);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}

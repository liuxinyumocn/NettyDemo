import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class MyHandler  extends SimpleChannelInboundHandler<FullHttpRequest>{

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
		//��������������յ���HTTP���ݣ�������Ǻ����Ĳ������������ͻ���
		String uri = req.uri();
		Map<String,String> resMap = new HashMap<>();
		resMap.put("method", req.method().name());
		resMap.put("uri", uri);
		String msg = "<html><head><title>test</title></head><body>������uriΪ��" + uri + "</body></html>";
		// ����http��Ӧ
		FullHttpResponse response = new DefaultFullHttpResponse(
		    HttpVersion.HTTP_1_1,
		    HttpResponseStatus.OK,
		    Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
		// ����ͷ��Ϣ
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
		// ��html
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		 ctx.flush();
	}


}

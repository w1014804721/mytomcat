package top.wangjingxin.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by 王镜鑫 on 2017/2/20 12:27.
 */
public class NioServerHandler implements Runnable {
    public static final Log log = LogFactory.getLog(NioServerHandler.class);
    private int port;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    public NioServerHandler(int port){
        this.port = port;
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port),1024);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        SelectionKey key = null;
        log.info("连接已经建立");
        while (true){
            try {
                selector.select(1000);
                Set<SelectionKey> keySet = selector.selectedKeys();
                Iterator<SelectionKey> it = keySet.iterator();
                while (it.hasNext()){
                    key = it.next();
                    it.remove();
                    handlerKey(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handlerKey(SelectionKey key) {
        if(key.isValid()){
            if(key.isAcceptable()){
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                try {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    sc.register(selector,SelectionKey.OP_READ);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(key.isReadable()){
                SocketChannel sc = (SocketChannel) key.channel();
                ByteBuffer requestBuffer = ByteBuffer.allocate(ContextProperties.BUFFER_SIZE);
                try {
                    int req = sc.read(requestBuffer);
                    if(req>0){
                        Context.contextRequest.putIfAbsent(System.identityHashCode(sc),new ArrayList<>());
                        requestBuffer.flip();
                        byte[] request = new byte[requestBuffer.remaining()];
                        requestBuffer.get(request);
                        Context.contextRequest.get(System.identityHashCode(sc)).add(request);
                    }
                    if(req<ContextProperties.BUFFER_SIZE){
                        int id = System.identityHashCode(sc);
                        List<byte[]>  completeRequest = Context.contextRequest.get(id);
                        Context.contextRequest.remove(id);
                        Context.es.execute(()-> RequestHandler.process(completeRequest,sc));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

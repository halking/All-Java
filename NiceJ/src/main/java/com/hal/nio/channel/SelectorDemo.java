package com.hal.nio.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import com.hal.utils.StringUtils;


public class SelectorDemo {
	private static int ports = 30;
	
    public static void main(String[] args) {
    	monitorChannel(ports);
	}
    
    public static void monitorChannel(int ports){
    	try {
    		ServerSocketChannel channel = ServerSocketChannel.open();
    		ServerSocket socket = channel.socket();
    		Selector selector = Selector.open();
    		socket.bind(new InetSocketAddress(ports));
    		channel.configureBlocking(false);
    		channel.register(selector, SelectionKey.OP_ACCEPT);
    		while (true) {
				int n = selector.select();
				if (0==n) {
					continue;
				}
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				while (it.hasNext()) {
					SelectionKey key = (SelectionKey) it.next();
					if (key.isAcceptable()) {
						ServerSocketChannel  server = (ServerSocketChannel)key.channel();
						SocketChannel  socketChannel = server.accept();
						if (null == socketChannel) {
							;
						}
					   socketChannel.configureBlocking(false);
					   socketChannel.register(selector, SelectionKey.OP_READ);
					}
					if (key.isReadable()) {
					   readDataFromSocket(key);
					}
					it.remove();
				}
			}
		} catch (IOException e) {
		}
    }
    
    public static void  readDataFromSocket(SelectionKey key) {
	     if (StringUtils.isNull(key)) {
			 return;
		}
	    
	}
}

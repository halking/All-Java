package com.hal.nio.buffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class bufferDemo {
	
	public static void main(String[] args) {
		bufferTest();
	}

	public static void bufferTest() {
		byte [] b = {'a','b','c','d','e','f','g','h','k','o'};
		ByteBuffer byteBuffer = ByteBuffer.allocate(127);
		System.out.println("position value:"+byteBuffer.position());
		System.out.println("limit value:"+byteBuffer.limit());
		System.out.println("capacity value:"+byteBuffer.capacity());
		for (byte i = 0; i < b.length; i++) {
			 byteBuffer.put(b[i]);
		}
		System.out.println("position value:"+byteBuffer.position());
		System.out.println("limit value:"+byteBuffer.limit());
		System.out.println("capacity value:"+byteBuffer.capacity());
		
		byteBuffer.flip();
		System.out.println("after flip buffer ----");
		System.out.println("position value:"+byteBuffer.position());
		System.out.println("limit value:"+byteBuffer.limit());
		System.out.println("capacity value:"+byteBuffer.capacity());
		
		for (int i = 0; i < 10; i++) {
			 System.out.println(byteBuffer.get(i));
		}
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
     
			 System.out.println(byteBuffer.get());
		
	}

	public static void sliceTest() {
		// 缓冲区分片
		ByteBuffer buffer = ByteBuffer.allocate(10);

		for (int i = 0; i < buffer.capacity(); i++) {
			buffer.put((byte) i);
		}
		buffer.position(3);
		buffer.limit(7);
		ByteBuffer slice = buffer.slice();
		for (int i = 0; i < slice.capacity(); i++) {
			byte b = slice.get(i);
			b *= 11;
			slice.put(i, b);
		}
		buffer.clear();
		while (buffer.remaining() > 0) {
			System.out.println(buffer.get());
		}
	}
}

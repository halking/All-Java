package com.hal.nio.channel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.hal.utils.FileConstant;

public class ScatterGatherChannel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ByteBuffer head = ByteBuffer.allocate(121);
		head.clear();
		for (int i = 0; i < 121; i++) {
			head.put((byte) i);
		}
		ByteBuffer body = ByteBuffer.allocate(127);
		body.clear();
		for (int i = 0; i < body.capacity(); i++) {
			body.put((byte)(i));
		}
		head.flip();
		body.flip();
		try {
			ByteBuffer[]  buffer = {head,body};
			FileOutputStream fos = new FileOutputStream(FileConstant.PATH_2);
			FileChannel channel = fos.getChannel();
			channel.write(buffer);
		} catch (IOException e) {
			// TODO: handle exception
		}
	}

}

package com.hal.nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.hal.utils.FileConstant;

public class FileChannelDemo {
 

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileAndBuffer();
	}
	public static void FileAndBuffer(){
		try {
			//读取文件
			FileInputStream fs = new FileInputStream(new File(FileConstant.PATH_1));
			FileChannel channel = fs.getChannel();
			ByteBuffer buffer =  ByteBuffer.allocate(1024);
			FileOutputStream fos = new FileOutputStream(FileConstant.PATH_2);
			FileChannel oc = fos.getChannel();
			while (true){
				buffer.clear();
			    int r = channel.read(buffer);
			    if (r==-1) {
					break;
				}
			    buffer.flip();
				oc.write(buffer);
			}
			//写入文件
		} catch (FileNotFoundException e) {
			// TODO: handle exception
		}catch (IOException e) {
			// TODO: handle exception
		}
	}
	public static void channelAnd2(){
		try {
			RandomAccessFile fromFile = new RandomAccessFile(FileConstant.PATH_1, "rw");
			FileChannel fromcl = fromFile.getChannel();
			RandomAccessFile toFile = new RandomAccessFile(FileConstant.PATH_2, "rw");
			FileChannel tocl = toFile.getChannel();
			int position = 0;
			long cap = fromcl.size();
			tocl.transferFrom(fromcl, position, cap);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO: handle exception
		}
		
	}
}

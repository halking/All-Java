package com.hal.nio.buffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.zip.CRC32;

import com.hal.utils.FileConstant;

public class CRC32Demo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("input stream ----");
		long start = System.currentTimeMillis();
		long sum = checkSumInputstream(FileConstant.PATH_3);
		long end = System.currentTimeMillis();
		System.out.println("校验和：" + Long.toHexString(sum));
		System.out.println("excuted time:" + (end - start) + "mil\n");
		
		System.out.println("bufferd input stream ----");
		start = System.currentTimeMillis();
		sum = checkSumBufferInputstream(FileConstant.PATH_3);
		end = System.currentTimeMillis();
		System.out.println("校验和：" + Long.toHexString(sum));
		System.out.println("excuted time:" + (end - start) + "mil\n");
		
		System.out.println(" Randaccess file ----");
		start = System.currentTimeMillis();
		sum = checkSumRandomInputstream(FileConstant.PATH_3);
		end = System.currentTimeMillis();
		System.out.println("校验和：" + Long.toHexString(sum));
		System.out.println("excuted time:" + (end - start) + "mil\n");
		
		System.out.println(" Mapped file ----");
		start = System.currentTimeMillis();
		sum = checkSumMappedInputstream(FileConstant.PATH_3);
		end = System.currentTimeMillis();
		System.out.println("校验和：" + Long.toHexString(sum));
		System.out.print("excuted time:" + (end - start) + "mil\n");
	}

	public static long checkSumInputstream(String path) {
		int c;
		CRC32 crc = new CRC32();
		try {
			InputStream ins = new FileInputStream(path);
			while ((c = ins.read()) != -1) {
				crc.update(c);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException ex) {
			// TODO: handle exception
		}
		return crc.getValue();
	}

	public static long checkSumBufferInputstream(String path) {
		int c;
		CRC32 crc = new CRC32();
		try {
			InputStream ins = new BufferedInputStream(new FileInputStream(path));
			while ((c = ins.read()) != -1) {
				crc.update(c);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException ex) {
			// TODO: handle exception
		}
		return crc.getValue();
	}

	@SuppressWarnings("resource")
	public static long checkSumRandomInputstream(String path) {
		int c;
		CRC32 crc = new CRC32();
		try {
			RandomAccessFile ranFile = new RandomAccessFile(new File(path), "r");
			long len = ranFile.length();
			for (int i = 0; i < len; i++) {
				ranFile.seek(i);
				c = ranFile.readByte();
				crc.update(c);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException ex) {
			// TODO: handle exception
		}
		return crc.getValue();
	}

	public static long checkSumMappedInputstream(String path) {
		int c;
		CRC32 crc = new CRC32();
		try {
			FileInputStream ins = new FileInputStream(path);
			FileChannel channel = ins.getChannel();
			int len = (int) channel.size();
			MappedByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, len);
			for (int i = 0; i < len; i++) {
				c = buffer.get(i);
				crc.update(c);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException ex) {
			// TODO: handle exception
		}
		return crc.getValue();
	}
}

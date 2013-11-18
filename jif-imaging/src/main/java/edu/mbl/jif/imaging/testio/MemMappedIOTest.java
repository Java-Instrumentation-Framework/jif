package edu.mbl.jif.imaging.testio;

// from: http://javarevisited.blogspot.com/2012/01/memorymapped-file-and-io-in-java.html#ixzz2Gqjfmizo
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MemMappedIOTest {

	private static int count = 100;

	public static void main(String[] args) throws Exception {

		byte[] img = new byte[2048 * 2048];
		for (int i = 0; i < img.length; i++) {
			img[i] = (byte) 128;
		}
		RandomAccessFile memoryMappedFile = new RandomAccessFile("largeFile.txt", "rw");

		//Mapping a file into memory
 		MappedByteBuffer out = memoryMappedFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, img.length*count);

		//Writing into Memory Mapped File
		for (int i = 0; i < count; i++) {
			out.put(img);
		}
		System.out.println("Writing to Memory Mapped File is completed");
		

		//reading from memory file in Java
//		for (int i = 0; i < 10; i++) {
//			System.out.print((char) out.get(i));
//		}
//		System.out.println("Reading from Memory Mapped File is completed");
	}

}

package main.java.cn.liubinbin.pan.conf;

/**
 *
 * @author liubinbin
 *
 */
public class Contants {

	public static final String PAN_SERVER_PORT = "pan.server.port";

	public static final int DEFAULT_CACHE_SERVER_PORT = 50503;

	public static final String PAN_NETTY_SERVER_THREAD_COUNT = "pan.netty.server.thread.count";

	public static final int DEFAULT_CACHE_NETTY_SERVER_THREAD_COUNT = 8;

	public static final String CACHE_SEGMENT_SIZE = "cache.segment.size";

	public static final int DEFAULT_CACHE_SEGMENT_SIZE = 132096;
	
	public static final String CACHE_TOTAL_SIZE = "cache.total.size";

	public static final int DEFAULT_TOTAL_SEGMENT_SIZE = 262144;

	public static final String SLOT_SIZE = "cache.slot.size";
	
	public static final String DEFAULT_SLOT_SIZE = "128,512,132096";
	
	public static final String FILE_SEPARATOR = "/";

}

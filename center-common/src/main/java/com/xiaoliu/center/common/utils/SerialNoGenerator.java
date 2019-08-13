/**
 *
 */
package com.xiaoliu.center.common.utils;

import net.jcip.annotations.ThreadSafe;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 流水号生成器，根据当前的时间来进行定义的流水号格式
 * <pre class="code">
 * Example 1:
 * String serialNo = SerialNoGenerator.instance ().next ();
 *
 * Example 2:
 * String serialNo = SerialNoGenerator.instance ().next ( "Order" );
 * </pre>
 */
@ThreadSafe
public class SerialNoGenerator {

	private final AtomicInteger number = new AtomicInteger ( 0 );
	public Date lastTime;

	private static SerialNoGenerator generator;
	private static Lock classLock = new ReentrantLock ();

	/**
	 * 流水号的前缀
	 * <p>为避免服务器重启，造成数据重复，故将时间精确到<b>分钟</b>数上，这样即可避免服务器哪怕在维护，也无法生成重复的数据</p>
	 */
	private SimpleDateFormat noPrefixFormat = new SimpleDateFormat ( "yyyyMMddhhmm" );
	/**
	 * 流水号后缀，目前只支持到百万级，除非请求量爆发式的增加即可修改源码
	 */
	private DecimalFormat noSuffixFormat = new DecimalFormat ( "0000000" );

	/**
	 * 私有构造方法，创建生成器的单例
	 */
	private SerialNoGenerator() {}

	public static SerialNoGenerator instance () {
		classLock.lock ();
		try {
			if ( generator == null ) {
				generator = new SerialNoGenerator ();
				return generator;
			}
			return generator;
		} finally {
			classLock.unlock ();
		}
	}

	/**
	 * 生成一个纯数字的唯一流水号
	 * @return 流水号
	 */
	public String next () {
		return next ( null );
	}

	/**
	 * 生成下一个唯一的流水号
	 * @param typePrefix 流水号前缀(根据不同的需求可自定义流水号前缀)
	 * @return 流水号
	 */
	public String next ( String typePrefix ) {
		if ( typePrefix == null ) {
			typePrefix = new String ();
		}
		classLock.lock ();

		Date curTime = new Date ();
		if ( lastTime == null ) {
			lastTime = curTime;
		}
		if ( !noPrefixFormat.format ( lastTime ).equals ( noPrefixFormat.format ( curTime ) ) ) {
			number.set ( 0 );
			lastTime = curTime;
		};
		number.getAndIncrement ();
		classLock.unlock ();
		return typePrefix + noPrefixFormat.format ( lastTime ) + getRandNum () + noSuffixFormat.format ( number );
	}

	/**
	 * 生成一个5位数的随机码
	 * @return
	 */
	private String getRandNum () {
		Random random = new Random ();
		Integer tmp = Math.abs ( random.nextInt () );
		return String.valueOf ( tmp % ( 100000 - 10000 + 1 ) + 10000 );
	}
}

package com.gdt.RPC;
public class Recursive {

	public static void main(String[] args) {
		int fib = fib(7);
		System.out.println(fib);
	}
	//斐波那契的任务
	static int i=1;
	private static int fib(int n) {
	    if (n == 0) return 0;
	    if (n == 1) return 1;
	    i++;
	    System.err.println(i);
	    return fib(n-1) + fib(n-2);
	}
	
}

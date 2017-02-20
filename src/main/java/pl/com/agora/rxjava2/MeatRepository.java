package pl.com.agora.rxjava2;

import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.Uninterruptibles;

public class MeatRepository {

	public String makeMeat(int number) {
		Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
		System.out.println(Thread.currentThread().getName() + ": meat repo");
		return "meat " + number;
	}
}

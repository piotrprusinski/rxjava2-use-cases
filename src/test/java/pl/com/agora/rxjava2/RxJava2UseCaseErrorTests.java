package pl.com.agora.rxjava2;

import java.util.concurrent.Callable;

import org.junit.Test;

import io.reactivex.Observable;

public class RxJava2UseCaseErrorTests {

	private void log(String msg) {
		System.out.println(Thread.currentThread().getName() + ": " + msg);
	}

	private void logError(Throwable e) {
		System.out.println(Thread.currentThread().getName() + ": error ->" + e);
	}

	@Test
	public void throwException() throws Exception {
		Observable<String> a = Observable.fromCallable(() -> {
			throw new NullPointerException();
		});

		a.subscribe(this::log, this::logError);
	}

	@Test
	public void throwExceptionAndStop() throws Exception {
		Observable<String> a = Observable.create((emiter) -> {
			emiter.onNext("1");
			emiter.onNext("2");
			emiter.onNext("3");
			emiter.onError(new NullPointerException());
			emiter.onNext("4");
		});

		a.subscribe(this::log); // nie pojawi się 4
	}

	@Test
	public void throwExceptionAndStop2() throws Exception {
		Observable<String> a = Observable.create((emiter) -> {
			emiter.onNext("1");
			emiter.onNext("2");
			emiter.onNext("3");
			emiter.onError(new NullPointerException());
			emiter.onNext("4");
		});
		a.onErrorReturn(error -> "");
		a.subscribe(this::log); // nie pojawi się 4
	}

	@Test
	public void throwExceptionAndGoOn() throws Exception {
		Observable<String> a = Observable.range(1, 4)
				.flatMap(i -> Observable.fromCallable(ifThreeThenThrow(i)).onErrorReturn(error -> "pusty"));
		a.subscribe(this::log); // pojawi sie 4
	}

	private Callable<String> ifThreeThenThrow(Integer i) {
		return () -> {
			if (i == 3) {
				throw new NullPointerException();
			} else {
				return "" + i;
			}
		};
	}

}

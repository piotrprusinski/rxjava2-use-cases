package pl.com.agora.rxjava2;

import io.reactivex.Observable;
import org.junit.Test;

import java.util.concurrent.Callable;

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
		Observable<String> a = Observable.create((emitter) -> {
			emitter.onNext("1");
			emitter.onNext("2");
			emitter.onNext("3");
			emitter.onError(new NullPointerException());
			emitter.onNext("4");
		});

		a.subscribe(this::log); // nie pojawi się 4
	}

	@Test
	public void throwExceptionAndStop2() throws Exception {
		Observable<String> a = Observable.create((emitter) -> {
			emitter.onNext("1");
			emitter.onNext("2");
			emitter.onNext("3");
			emitter.onError(new NullPointerException());
			emitter.onNext("4");
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

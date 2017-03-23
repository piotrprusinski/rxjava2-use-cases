package pl.com.agora.rxjava2;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.util.StopWatch;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Uninterruptibles;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class Rxjava2UseCasesApplicationTests {

    private <T> void log(T msg) {
        System.out.println(Thread.currentThread().getName() + ": " + msg);
    }

    private void sleep(int i) {
        Uninterruptibles.sleepUninterruptibly(i, TimeUnit.SECONDS);
    }

    @Test
    public void shouldCreateSimpleObservable() {
        Observable.just("T").subscribe(this::log);
    }

    @Test
    public void shouldCreateSimpleObservableWithMethodCreate() throws Exception {
        Observable<String> observable = Observable.create(observableEmitter -> {
            observableEmitter.onComplete();
            observableEmitter.onNext("T2");
        });

        observable.subscribe(t -> log(t));
    }

    @Test
    public void shouldCreateSimpleObservableFromList() {
        Observable.just(Lists.newArrayList("t1", "t2", "t3"))
                .map(s -> {
                    System.out.println(s);
                    return s;
                })
                .subscribe(this::log);
    }

    @Test
    public void shouldCreateSimpleObservableFromList2() {
        Observable.fromIterable(Lists.newArrayList("t1", "t2", "t3")).subscribe(this::log);
    }

    @Test
    public void shouldRunTaskInThread() {
        Observable.fromIterable(Lists.newArrayList("t1", "t2", "t3"))
                .subscribeOn(Schedulers.from(ForkJoinPool.commonPool()))
                .subscribe(this::log);
        //sleep(1);
    }

    @Test
    public void shouldMakeKebab() throws Exception {
        Observable<String> meatObservable = Observable
                .fromIterable(Lists.newArrayList("meat 1", "meat 2", "meat 3", "meat 4"));
        Observable<String> breadObservable = Observable
                .fromIterable(Lists.newArrayList("bread 1", "bread 2", "bread 3", "bread 4"));
        Observable<String> vegetablesObservable = Observable
                .fromIterable(Lists.newArrayList("vegetables 1", "vegetables 2", "vegetables 3"));

        Observable.zip(meatObservable,
                breadObservable,
                vegetablesObservable,
                (m, b, v) -> String.format("%s, %s, %s", m, b, v))
                .subscribe(this::log);
    }

    private final MeatRepository meatRepo = new MeatRepository();

    @Test
    public void shouldMakeKebabSync() throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Observable<String> meatObservable = Observable.range(1, 3)
                .flatMap(i -> Observable.fromCallable(() -> meatRepo.makeMeat(i)));

        Observable<String> breadObservable = Observable
                .fromIterable(Lists.newArrayList("bread 1", "bread 2", "bread 3", "bread 4"));
        Observable<String> vegetablesObservable = Observable
                .fromIterable(Lists.newArrayList("vegetables 1", "vegetables 2", "vegetables 3"));

        Observable.zip(meatObservable,
                breadObservable,
                vegetablesObservable,
                (m, b, v) -> String.format("%s, %s, %s", m, b, v)).subscribe(this::log);
        stopWatch.stop();
        log(stopWatch.getTotalTimeSeconds() + "s");
    }

    @Test
    public void shouldMakeKebabNotWorkingAsync() throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Observable<String> meatObservable = Observable
                .range(1, 3)
                .flatMap(i -> Observable.fromCallable(() -> meatRepo.makeMeat(i)))
                .subscribeOn(Schedulers.io());

        Observable<String> breadObservable = Observable
                .fromIterable(Lists.newArrayList("bread 1", "bread 2", "bread 3", "bread 4"));
        Observable<String> vegetablesObservable = Observable
                .fromIterable(Lists.newArrayList("vegetables 1", "vegetables 2", "vegetables 3"));

        Observable.zip(meatObservable,
                breadObservable,
                vegetablesObservable,
                (m, b, v) -> String.format("%s, %s, %s", m, b, v)).subscribe(this::log);
        stopWatch.stop();
        log(stopWatch.getTotalTimeSeconds() + "s");
        sleep(5);
    }

    @Test
    public void shouldMakeKebabAsyncParaller() throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Observable<String> meatObservable = Observable
                .range(1, 3)
                .flatMap(i -> Observable.fromCallable(() -> meatRepo.makeMeat(i))
                        .subscribeOn(Schedulers.io()));

        Observable<String> breadObservable = Observable
                .fromIterable(Lists.newArrayList("bread 1", "bread 2", "bread 3", "bread 4"));
        Observable<String> vegetablesObservable = Observable
                .fromIterable(Lists.newArrayList("vegetables 1", "vegetables 2", "vegetables 3"));

        Observable.zip(meatObservable,
                breadObservable,
                vegetablesObservable,
                (m, b, v) -> String.format("%s, %s, %s", m, b, v)).subscribe(this::log);
        stopWatch.stop();
        log(stopWatch.getTotalTimeSeconds() + "s");
        sleep(5);
    }

}

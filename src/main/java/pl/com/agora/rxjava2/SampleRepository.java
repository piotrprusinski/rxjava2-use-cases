package pl.com.agora.rxjava2;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

import java.util.concurrent.TimeUnit;

/**
 * Created by pprusinski on 2017-02-21.
 */
public class SampleRepository {

    private Scheduler scheduler;

    public SampleRepository(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public Observable<String> getName() {
        return Observable
                .just("my name")
                .delay(10, TimeUnit.SECONDS, scheduler);
    }
}

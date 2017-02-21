package pl.com.agora.rxjava2;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class RxJavaTestScheduler {

    private TestScheduler testScheduler = new TestScheduler();
    private SampleRepository sampleRepository = new SampleRepository(testScheduler);


    @Test
    public void shouldRuntTestOnSchedule() {
        //given

        //when
        Observable<String> nameObs = sampleRepository.getName();//sleep 10 seconds
        //then
        nameObs.subscribeOn(testScheduler);

        TestObserver<String> testObserver = nameObs.test();

        testObserver.assertEmpty();

        testScheduler.advanceTimeBy(12, TimeUnit.SECONDS);
        testObserver.assertValue("my name");
    }
}

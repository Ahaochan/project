package moe.ahao.spring.cloud.hystrix;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

public class RxJavaTest {
    @Test
    public void observable() {
        Observable.just(1, 2, 3)
        // Observable.error(new RuntimeException())
            .doOnSubscribe(new Action0() {
                @Override
                public void call() {
                    System.out.println("1doOnSubscribe");
                }
            })
            .doOnNext(new Action1<Object>() {
                @Override
                public void call(Object integer) {
                    System.out.println("2doOnNext");
                }
            })
            .doOnCompleted(new Action0() {
                @Override
                public void call() {
                    System.out.println("4doOnCompleted");
                }
            })
            .doOnError(new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    System.out.println("4doOnError");
                }
            })
            .doOnTerminate(new Action0() {
                @Override
                public void call() {
                    System.out.println("5doOnTerminate");
                }
            })
            .doOnUnsubscribe(new Action0() {
                @Override
                public void call() {
                    System.out.println("7doOnUnsubscribe");
                }
            })
            .doAfterTerminate(new Action0() {
                @Override
                public void call() {
                    System.out.println("8doAfterTerminate");
                }
            })
            .subscribe(new Subscriber<Object>() {
                @Override
                public void onNext(Object obj) {
                    System.out.println("3onNext: " + obj);
                }
                @Override
                public void onCompleted() {
                    System.out.println("6onCompleted");
                }
                @Override
                public void onError(Throwable e) {
                    System.out.println("6onError");
                }
            });
    }
}

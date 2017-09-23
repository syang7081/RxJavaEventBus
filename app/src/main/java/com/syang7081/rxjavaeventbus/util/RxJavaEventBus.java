package com.syang7081.rxjavaeventbus.util;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by syang7081@gmail.com
 */

public class RxJavaEventBus {
    private static final String tag = RxJavaEventBus.class.getSimpleName();
    private static final RxJavaEventBus instance;
    private Map<Consumer<? super BaseEvent>, Disposable> consumerDisposableMap;
    private PublishSubject<BaseEvent> publishSubject;

    static {
        instance = new RxJavaEventBus();
    }

    private RxJavaEventBus() {
        consumerDisposableMap = new HashMap<>();
        publishSubject = PublishSubject.create();
    }

    public static <T extends BaseEvent> void register(Consumer<? super BaseEvent> consumer) {
        if (consumer == null || instance.consumerDisposableMap.containsKey(consumer)) return;
        Disposable disposable = instance.publishSubject.observeOn(AndroidSchedulers.mainThread())
                                .subscribe(consumer);
        instance.consumerDisposableMap.put(consumer, disposable);
        Log.d(tag, "Registered consumer: " + consumer);
    }

    public static void unregister(Consumer<? super BaseEvent> consumer) {
        if (consumer  == null) return;
        Disposable disposable = instance.consumerDisposableMap.get(consumer);
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            Log.d(tag, "Unregistered consumer: " + consumer);
        }
    }

    public static void post(BaseEvent event) {
        if (event == null) return;
        instance.publishSubject.onNext(event);
        Log.d(tag, "Fired event: " + event);
    }
}

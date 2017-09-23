/**
 * Copyright 2017 Shawn Yang.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package com.syang7081.rxjavaeventbus.util;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class RxJavaEventBus {
    private static final String tag = RxJavaEventBus.class.getSimpleName();
    private static final RxJavaEventBus instance;
    private Map<Consumer<BaseEvent>, Disposable> consumerDisposableMap;
    private PublishSubject<BaseEvent> publishSubject;

    static {
        instance = new RxJavaEventBus();
    }

    private RxJavaEventBus() {
        consumerDisposableMap = new HashMap<>();
        publishSubject = PublishSubject.create();
    }

    public static <T extends BaseEvent> void register(Consumer<BaseEvent> consumer, Class<T> eventCLass) {
        if (consumer == null || instance.consumerDisposableMap.containsKey(consumer)) return;
        Disposable disposable = instance.publishSubject.observeOn(AndroidSchedulers.mainThread())
                .ofType(eventCLass)
                .subscribe(consumer);
        instance.consumerDisposableMap.put(consumer, disposable);
        Log.d(tag, "Registered consumer: " + consumer);
    }

    public static <T extends BaseEvent> void register(Consumer<BaseEvent> consumer, Class<T> eventCLass,
                                                      Scheduler observeOnScheduler) {
        if (consumer == null || instance.consumerDisposableMap.containsKey(consumer)) return;
        Disposable disposable = instance.publishSubject.observeOn(observeOnScheduler)
                .ofType(eventCLass)
                .subscribe(consumer);
        instance.consumerDisposableMap.put(consumer, disposable);
        Log.d(tag, "Registered consumer: " + consumer + ", observeOn: " + observeOnScheduler);
    }

    public static void unregister(Consumer<BaseEvent> consumer) {
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

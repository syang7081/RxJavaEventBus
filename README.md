# RxJavaEventBus
A simple event bus based on RxJava 2.0 for Android

## Sample usage

A sample code to use the event bus is provided in the MainActivity. The basic procedures to use the event bus are as follows:

(1) Declare an event to listener to
```java
class MyEvent implements BaseEvent { /* event content */}
```

(2) Declare a consumer for the event
```java
class MyConsumer implements Consumer<MyEvent> {
    public void accept(MyEvent event) throws Exception { /* handle your event here */}
}

// ....

// Declare in your activity or fragment
private Consumer myConsumer = new MyConsumer();
```

(3) Register the consumer and event
```java
/* in an activity or fragment  */
public void onResume() {
    super.onResume();
    RxJavaEventBus.register(myConsumer, MyEvent.class);
}
```

(4) Unregister the consumer and event
```java
/* in the same activity or fragment  */
public void onPause() {
    super.onPause();
    RxJavaEventBus.unregister(myConsumer);
}
```

(5) Post the event anywhere in any thread
```java
MyEvent myEvent1 = new MyEvent();
RxJavaEventBus.post(myEvent1);
```

## LICENSE

    Copyright 2017 Shawn Yang

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
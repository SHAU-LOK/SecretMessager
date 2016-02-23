package com.shootloking.secretmessager.utility;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by shau-lok on 2/23/16.
 */
public class RxUtils {


    public static Observable<String> testRxJava() {
        Observable<String> myObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello World");
                subscriber.onCompleted();
            }
        });
        return myObservable;
    }


//    public static Observable<Cursor> getConversationListCursor(Context context) {
//        Observable<Cursor> myObservable = Observable.create(new Observable.OnSubscribe<Cursor>() {
//            @Override
//            public void call(Subscriber<? super Cursor> subscriber) {
//
//            }
//        })
//        return null;
//    }

}

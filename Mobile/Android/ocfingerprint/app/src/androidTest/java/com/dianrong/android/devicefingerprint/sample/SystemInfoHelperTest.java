package io.ocnet.android.devicefingerprint.sample;

import android.os.Looper;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.android.schedulers.AndroidSchedulers;
import rx.observers.TestSubscriber;

@RunWith(AndroidJUnit4.class)
public class SystemInfoHelperTest {

    /*
     * 由于SystemInfoHelper获取系统信息需要用到安卓的Context，为了测试，
     * 先启动一个activity
     */

    SystemInfoHelper helper;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup() {
        helper = new SystemInfoHelper(rule.getActivity());
    }

    @Test
    public void test_isRoot() {
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();

        helper.isRoot()
                .observeOn(AndroidSchedulers.mainThread())
                .toBlocking()
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        Assert.assertTrue(testSubscriber.getOnNextEvents().size() == 1);
        Assert.assertTrue(testSubscriber.getLastSeenThread().getId()
                != Looper.getMainLooper().getThread().getId());
    }

    @Test
    public void test_getDeviceInfo() {
        TestSubscriber<DeviceInfo> testSubscriber = TestSubscriber.create();

        helper.getDeviceInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .toBlocking()
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        Assert.assertTrue(testSubscriber.getOnNextEvents().size() == 1);
        Assert.assertTrue(testSubscriber.getOnNextEvents().get(0) != null);
        Assert.assertTrue(testSubscriber.getLastSeenThread().getId()
                != Looper.getMainLooper().getThread().getId());
    }

    @Test
    public void test_getCpuProcessor() {
        TestSubscriber<String> testSubscriber = TestSubscriber.create();

        helper.getCpuProcessor()
                .toBlocking()
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        Assert.assertTrue(testSubscriber.getOnNextEvents().size() == 1);
        Assert.assertTrue(!TextUtils.isEmpty(testSubscriber.getOnNextEvents().get(0)));
        Assert.assertTrue(testSubscriber.getLastSeenThread().getId()
                != Looper.getMainLooper().getThread().getId());
    }

    @Test
    public void test_getGooglePlayServiceAdId() {
        TestSubscriber<String> subscriber = TestSubscriber.create();

        helper.getGooglePlayServiceAdId()
                .toBlocking()
                .subscribe(subscriber);

        Assert.assertTrue(subscriber.getLastSeenThread().getId()
                != Looper.getMainLooper().getThread().getId());
    }

}

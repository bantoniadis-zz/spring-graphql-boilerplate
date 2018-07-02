package com.thoughtmechanix.licenses.hystrix;

import com.thoughtmechanix.licenses.utils.UserContext;
import com.thoughtmechanix.licenses.utils.UserContextHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.concurrent.Callable;

/* When a call is made to a Hystrix protected method, Hystrix and Spring Cloud will instantiate an instance of the
 * DelegatingUserContextCallable class, passing in the Callable class that would normally be invoked by a thread
 * managed by a Hystrix command pool. In the previous listing, this Callable class is stored in a Java property called
 * delegate. Conceptually, you can think of the delegate property as being the handle to the method protected by a
 * @HystrixCommand annotation.
 */
public final class DelegatingUserContextCallable<V> implements Callable<V> {
    private static final Logger logger = LoggerFactory.getLogger(DelegatingUserContextCallable.class);
    private final Callable<V> delegate;

    private UserContext originalUserContext;

    /*
     * This custom Callable class will be passed the original Callable class that will invoke our Hytrix protected
     * code and UserContext coming in from the parent thread.
     */
    public DelegatingUserContextCallable(Callable<V> delegate, UserContext userContext) {
        Assert.notNull(delegate, "delegate cannot be null");
        Assert.notNull(userContext, "userContext cannot be null");
        this.delegate = delegate;
        this.originalUserContext = userContext;
    }

    /*
     * Once the UserContext is set, invoke the call() method on the Hystrix protected method:
     * for instance, our LicenseServer.getLicenseByOrg() method.
     */
    public V call() throws Exception {
        UserContextHolder.setContext(originalUserContext);

        try {
            return delegate.call();
        }
        finally {
            this.originalUserContext = null;
        }
    }

    public String toString() {
        return delegate.toString();
    }

    public static <V> Callable<V> create(Callable<V> delegate, UserContext userContext) {
        return new DelegatingUserContextCallable<V>(delegate, userContext);
    }
}


package org.softauto.core;

import java.util.concurrent.*;

/**
 * A Future implementation for RPCs base on Avro.
 */
public class CallFuture<T> implements Future<T>, Callback<T> {
  private final CountDownLatch latch = new CountDownLatch(1);
  private final Callback<T> chainedCallback;
  private T result = null;
  private Throwable error = null;
  private Object[] arguments = null;
  private Class[] argumentsType = null;
  private boolean succeeded= false;
  private boolean failed = false;
  private Throwable cause;


  /**
   * Creates a CallFuture.
   */
  public CallFuture() {
    this(null);
  }

  /**
   * Creates a CallFuture with a chained Callback which will be invoked when this
   * CallFuture's Callback methods are invoked.
   * 
   * @param chainedCallback the chained Callback to set.
   */
  public CallFuture(Callback<T> chainedCallback) {
    this.chainedCallback = chainedCallback;
  }


  public CallFuture handleArguments(Object[] arguments){
    this.arguments = arguments;
    return this;
  }

  public CallFuture handleArgumentsType(Class[] argumentsType){
    this.argumentsType = argumentsType;
    return this;
  }

  /**
   * Sets the RPC response, and unblocks all threads waiting on {@link #get()} or
   * {@link #get(long, TimeUnit)}.
   *
   * @param result the RPC result to set.
   */

  public void handleResult(T result) {
    this.result = result;
    latch.countDown();
    if (chainedCallback != null) {
      chainedCallback.handleResult(result);
    }
    succeeded = true;
    TestContext.setStepState(StepLifeCycle.STOP);
  }



  /**
   * Sets an error thrown during RPC execution, and unblocks all threads waiting
   * on {@link #get()} or {@link #get(long, TimeUnit)}.
   * 
   * @param error the RPC error to set.
   */

  public void handleError(Throwable error) {
    this.error = error;
    latch.countDown();
    if (chainedCallback != null) {
      chainedCallback.handleError(error);
    }
    failed = true;
    cause = error;
    TestContext.setStepState(StepLifeCycle.STOP);
  }

  public Object[] getArguments() {
    return arguments;
  }

  public Class[] getArgumentsType() {
    return argumentsType;
  }

  /**
   * Gets the value of the RPC result without blocking. Using {@link #get()} or
   * {@link #get(long, TimeUnit)} is usually preferred because these methods block
   * until the result is available or an error occurs.
   * 
   * @return the value of the response, or null if no result was returned or the
   *         RPC has not yet completed.
   */
  public T getResult() {
    return result;
  }

  /**
   * Gets the error that was thrown during RPC execution. Does not block. Either
   * {@link #get()} or {@link #get(long, TimeUnit)} should be called first because
   * these methods block until the RPC has completed.
   * 
   * @return the RPC error that was thrown, or null if no error has occurred or if
   *         the RPC has not yet completed.
   */
  public Throwable getError() {
    return error;
  }

  @Override
  public boolean cancel(boolean mayInterruptIfRunning) {
    return false;
  }

  @Override
  public boolean isCancelled() {
    return false;
  }

  @Override
  public T get() throws InterruptedException, ExecutionException {
    latch.await();
    if (error != null) {
      throw new ExecutionException(error);
    }
    return result;
  }

  @Override
  public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    if (latch.await(timeout, unit)) {
      if (error != null) {
        throw new ExecutionException(error);
      }
      return result;
    } else {
      throw new TimeoutException();
    }
  }

  /**
   * Waits for the CallFuture to complete without returning the result.
   * 
   * @throws InterruptedException if interrupted.
   */
  public void await() throws InterruptedException {
    latch.await();
  }

  /**
   * Waits for the CallFuture to complete without returning the result.
   * 
   * @param timeout the maximum time to wait.
   * @param unit    the time unit of the timeout argument.
   * @throws InterruptedException if interrupted.
   * @throws TimeoutException     if the wait timed out.
   */
  public void await(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
    if (!latch.await(timeout, unit)) {
      throw new TimeoutException();
    }
  }

  @Override
  public boolean isDone() {
    return latch.getCount() <= 0;
  }


}

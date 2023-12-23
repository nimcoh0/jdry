

package org.softauto.core;

/**
 * Interface for receiving asynchronous callbacks. For each request with an
 * asynchronous callback, either {@link #handleResult(Object)} or
 * {@link #handleError(Throwable)} will be invoked.
 */
public interface Callback<T> extends org.apache.avro.ipc.Callback<T>{
  /**
   * Receives a callback result.
   *
   * @param result the result returned in the callback.
   */
  void handleResult(T result);

  /**
   * Receives an error.
   * 
   * @param error the error returned in the callback.
   */
  void handleError(Throwable error);
}

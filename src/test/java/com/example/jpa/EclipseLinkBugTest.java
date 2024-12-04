package com.example.jpa;

import static com.example.jpa.User.BY_NAME_AND_EMAIL;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.persistence.sessions.UnitOfWork;
import org.junit.Test;

public class EclipseLinkBugTest {

  private final static long ITERATIONS = 50000;

  private final static long DEFAULT_USER_ID = -1L;
  private final static String DEFAULT_USER = "user";
  private final static String DEFAULT_EMAIL = "user@mail.com";

  @Test
  public void testConcurrentException() {
    DBSessionProvider.getInstance().getEntityManagerFactory().getCache().evictAll();

    AtomicLong offsetValue = new AtomicLong(1);
    AtomicBoolean isRunning = new AtomicBoolean(true);
    AtomicBoolean success = new AtomicBoolean(true);

    UnitOfWork uow1 = DBSessionProvider.getInstance().getNewClientSession().acquireUnitOfWork();
    uow1.registerNewObject(new User(DEFAULT_USER_ID, DEFAULT_USER, DEFAULT_EMAIL));
    uow1.commit();

    Thread updateThread = new Thread(() -> {
      try {
        UnitOfWork uow = DBSessionProvider.getInstance().getNewClientSession().acquireUnitOfWork();
        for (int i = 0; i < ITERATIONS; i++) {
          User firstUser = (User) uow.executeQuery(BY_NAME_AND_EMAIL, DEFAULT_USER, DEFAULT_EMAIL);
          firstUser.getAddresses().add(new Address(offsetValue.incrementAndGet() * -1, "str", String.valueOf(offsetValue.get()), "city"));

          User newUser = new User(offsetValue.get(), "user" + offsetValue.get(), "user" + offsetValue.get() + "@mail.com");
          newUser.getAddresses().add(new Address(offsetValue.get(), "str", String.valueOf(offsetValue.get()), "city"));
          uow.registerNewObject(newUser);

          ((User) uow.executeQuery(BY_NAME_AND_EMAIL, DEFAULT_USER, DEFAULT_EMAIL)).getAddresses().size();
        }
        uow.commit();
      } catch (Exception e) {
        e.printStackTrace();
        success.set(false);
      } finally {
        isRunning.set(false);
      }
    });

    Thread queryThread = new Thread(() -> {
      while (isRunning.get()) {
        try {
          UnitOfWork uow = DBSessionProvider.getInstance().getNewClientSession().acquireUnitOfWork();
        uow.executeQuery(BY_NAME_AND_EMAIL, DEFAULT_USER, DEFAULT_EMAIL);
          uow.commit();
        } catch (Exception e) {
          e.printStackTrace();
          success.set(false);
          return;
        }
      }
    });

    queryThread.start();
    updateThread.run();

    assertTrue(success.get());

  }

}
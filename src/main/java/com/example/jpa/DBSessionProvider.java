package com.example.jpa;

import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.server.ServerSession;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DBSessionProvider {

  private static DBSessionProvider singleton = null;

  public static synchronized DBSessionProvider getInstance() {
    if (singleton == null) {
      singleton = new DBSessionProvider();
    }
    return singleton;
  }

  private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("test-jpa");

  protected ServerSession getServerSession() {
    return entityManagerFactory.unwrap(ServerSession.class);
  }

  public Session getNewClientSession() {
    return getServerSession().acquireClientSession();
  }

  public EntityManagerFactory getEntityManagerFactory() {
    return entityManagerFactory;
  }
}
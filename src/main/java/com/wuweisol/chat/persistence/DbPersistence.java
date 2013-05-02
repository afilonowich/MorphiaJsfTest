package com.wuweisol.chat.persistence;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

import javax.inject.Named;
import javax.persistence.Entity;

/**
 */

@Named
public class DbPersistence
{
   private static final DbPersistence INSTANCE = new DbPersistence();

//   private final Datastore db;
   private final Morphia morphia;
   private Mongo mongo;
   public static final String DB_NAME = "GmdcChatTest";

   public DbPersistence()
   {
System.out.println("Creating DB Persistence");
      try
      {
         mongo = new Mongo("localhost", 27017);
         morphia = new Morphia();

//         db = new Morphia().map(Order.class).map(LineItem.class).map(Customer.class).createDatastore(
//           m, DB_NAME);
//         db.ensureIndexes();
      }
      catch(Exception e)
      {
         throw new RuntimeException("Error initializing mongo db", e);
      }
   }

   public static DbPersistence instance()
   {
      return INSTANCE;
   }

   public void mapClass(Class clazz)
   {
      Morphia morphia = new Morphia();
      morphia.map(clazz);
   }

   public Datastore getDatastore()
   {
      return morphia.createDatastore(mongo, DB_NAME);
   }

   public Morphia getMorphia()
   {
      return morphia;
   }

   public Mongo getMongo()
   {
      return mongo;
   }

   public static String getDbName()
   {
      return DB_NAME;
   }
}

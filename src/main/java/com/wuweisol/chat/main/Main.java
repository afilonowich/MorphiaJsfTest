package com.wuweisol.chat.main;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.wuweisol.chat.model.Conversation;
import com.wuweisol.chat.model.Message;
import com.wuweisol.chat.persistence.DbPersistence;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class Main
{
   DbPersistence dbPersistence;

   public static void main(String[] args)
   {
      // For XML
      // ApplicationContext ctx = new GenericXmlApplicationContext("mongo-config.xml");

      // For Annotation
//      ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
//      MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

      Main main = new Main();
      Conversation conversation;

      Boolean deleteOnly = false;
      Boolean create = true;
      Boolean update = true;

      String chat1 = "Aaron";
      String chat2 = "Chuck";
      String conversationId = "103";

      if(deleteOnly) // Delete only
      {
         main.deleteConversation(conversationId);

         return;
      }

//      main.deleteConversation("515914c86474fb503160dc44");
      if(create)
         conversation = main.createConversation(conversationId, chat1, chat2);

      List<Conversation> conversations = main.getConversations("Grand Event", chat1);
      conversation = main.getConversation(conversationId);
      if(conversation != null)
         System.out.println("Conversation found by id");
      else
         System.out.println("Conversation NOT found by id");
//      main.deleteConversation(conversation);
//      main.deleteConversation(conversationId);
//      conversation = main.findConversation(conversationId);
//      if(conversation != null)
//         System.out.println("2nd find Conversation found by id");
//      else
//         System.out.println("2nd find Conversation NOT found by id");

      if(update)
      {
         main.updateConversation(conversation, chat1, "Updated Add message " + conversation.getMessages().size());
         main.updateConversation(conversation, chat2, "Updated Add message " + conversation.getMessages().size());
//      conversation = main.createConversation(conversationId);
      }
   }

   public Main()
   {
      dbPersistence = new DbPersistence();

      dbPersistence.getMorphia().map(Conversation.class);
   }

   public DbPersistence getDbPersistence()
   {
      return dbPersistence;
   }

   public List<Conversation> getConversations(String event, String chatter)
   {
      Query<Conversation> q = dbPersistence.getDatastore().createQuery(Conversation.class)
            .disableValidation()
            .filter("event", event)
            .filter("className", Conversation.class.getName());

            q.field("parties").contains(chatter);
//            q.field("parties").contains(chat2);
//            q.field("parties").contains("Unknown Key");

      List<Conversation> conversations = q.asList();

      return conversations;
   }

   public Conversation getConversation(String id)
   {
      Conversation conversation = null;

      Query<Conversation> q = dbPersistence.getDatastore().createQuery(Conversation.class)
            .disableValidation()
            .filter("_id", id)
            .filter("className", Conversation.class.getName());

      conversation = q.get();
/*
      conversation = mongoOperation.findOne(new Query(Criteria.where("conversationId").is(id)), Conversation.class);

      if(conversation != null)
         System.out.println("Found id " + id + " with conversation " + conversation.getConversationId());
      else
         System.out.println("Could not find conversation id " + id);
*/

      return conversation;
   }

   public void deleteConversation(String id)
   {
//      mongoOperation.remove(new Query(Criteria.where("conversationId").is(id)), Conversation.class);
      Conversation conversation = getConversation(id);
      if(conversation != null)
      {
         dbPersistence.getDatastore().delete(conversation);
         System.out.println("Converstaion id = [" + id + "] should be deleted by id");
      }
      else
         System.out.println("Cannot find conversation by id " + id);
   }

   public void deleteConversation(Conversation conversation)
   {
//      mongoOperation.remove(conversation);
      System.out.println("Converstaion id = [" + conversation.getConversationId() + "] should be deleted by Object");
   }

   public Conversation createConversation(String id, String chat1, String chat2)
   {
      Conversation conversation = new Conversation();
      conversation.setConversationId(id);
      conversation.setEvent("Grand Event");
      List<String> parties = new ArrayList<String>();
      parties.add(chat1);
      parties.add(chat2);
      conversation.setPartyIds(parties);

      Message msg = new Message();
      msg.setPartyPoster(chat1);
      msg.setMessage("How are you?");
      conversation.addMessage(msg);

      Message msg2 = new Message();
      msg2.setPartyPoster(chat2);
      msg2.setMessage("Doing well... and yourself?");
      conversation.addMessage(msg2);

      msg = new Message();
      msg.setPartyPoster(chat1);
      msg.setMessage("That is insane!");
      conversation.addMessage(msg);

      msg2 = new Message();
      msg2.setPartyPoster(chat2);
      msg2.setMessage("I know right!");
      conversation.addMessage(msg2);

//      mongoOperation.save(conversation);
      dbPersistence.getDatastore().save(conversation);

      return conversation;
   }

   public void updateConversation(Conversation conversation, String poster, String message)
   {
      Message msg = new Message();
      msg.setPartyPoster(poster);
      msg.setMessage(message);

      conversation.addMessage(msg);

      Datastore ds = dbPersistence.getDatastore();
//      ds.merge(conversation.getMessages());

      Query<Conversation> query = ds.createQuery(Conversation.class).field("_id").equal(conversation.getConversationId());
//      Query<Conversation> query = ds.createQuery(Conversation.class).field("_id").equal(conversation.getConversationId());
      UpdateOperations<Conversation> updateOperations =  ds.createUpdateOperations(Conversation.class).add("messages", msg);
//            disableValidation().add("messages.$.fileHash", hash).enableVali..;
//            disableValidation().set("fileObjects.$.fileHash", hash).enableVali..;

      ds.update(query, updateOperations);
//      dbPersistence.getDatastore().update(ds.createQuery(Conversation.class))
//      mongo.update(mongo.createQuery(BatchData.class)
//            .filter("uuid",theBatch.uuid)
//            .filter("fileObjects.fileName","theFileName"),updateOperations);
//      dbPersistence.getDatastore().update()

      /*
      DBObject dbo = new BasicDBObject();
      mongoOperation.getConverter().write(msg, dbo);

      System.out.println("Update message is " + message);
      System.out.println("Converted DBObject message is " + dbo);

//      mongoOperation.updateFirst(conversation,
      String id = conversation.getConversationId();
      System.out.println("Update conversation = [" + conversation + "], by poster = [" + poster + "], with message = [" + message + "]");
      try
      {
         mongoOperation.updateFirst(new Query(Criteria.where("conversationId").is(id)),
               new Update().push("messages", dbo), Conversation.class);
      }
      catch(Exception ex)
      {
         System.out.println("Exception updating conversation " + ex);
         ex.printStackTrace();
      }
   */
   }

/*
      User user = new User("mkyong", "password123");

      // save
      mongoOperation.save(user, "users");

      // find
      User savedUser = mongoOperation.findOne(new Query(Criteria.where("username").is("mkyong")), User.class, "users");

      System.out.println("savedUser : " + savedUser);

      // update
      mongoOperation.updateMulti(new Query(Criteria.where("username").is("mkyong")),
            Update.update("password", "new password"), "users");

      // find
      User updatedUser = mongoOperation.findOne(new Query(Criteria.where("username").is("mkyong")), User.class, "users");

      System.out.println("updatedUser : " + updatedUser);

      // delete
      mongoOperation.remove(new Query( Criteria.where("username").is("mkyong")), "users");

      // List1G
      List<User> listUser = mongoOperation.findAll(User.class, "users");
      System.out.println("Number of user = " + listUser.size());
   }
*/
}

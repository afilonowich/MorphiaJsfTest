package com.wuweisol.chat.persistence;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.dao.BasicDAO;
import com.google.code.morphia.emul.org.bson.types.ObjectId;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import com.wuweisol.chat.model.Conversation;
import com.wuweisol.chat.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class ConversationDAO
      extends BasicDAO<Conversation, ObjectId>
{
   DbPersistence dbPersistence;

   public ConversationDAO(DbPersistence dbPersistence)
   {
      super(dbPersistence.getMongo(), dbPersistence.getMorphia(), dbPersistence.getDbName());

      this.dbPersistence = dbPersistence;
   }

   public List<Conversation> getConversations(String event, String chatId1)
   {
      Query<Conversation> q = dbPersistence.getDatastore().createQuery(Conversation.class)
            .disableValidation()
            .filter("event", event)
            .filter("className", Conversation.class.getName());

      List<Conversation> conversations = q.asList();

      return conversations;
   }

   public Conversation getConversation(String event, String chatter1, String chatter2)
   {
      Query<Conversation> q = dbPersistence.getDatastore().createQuery(Conversation.class)
            .disableValidation()
            .filter("event", event)
            .filter("className", Conversation.class.getName());

      q.field("parties").contains(chatter1);
      q.field("parties").contains(chatter2);
//            q.field("parties").contains(chat2);
//            q.field("parties").contains("Unknown Key");

      Conversation conversations = q.get();

      return conversations;
   }

   public void updateConversation(Conversation conversation, String poster, String message)
   {
      Message msg = new Message();
      msg.setPartyPoster(poster);
      msg.setMessage(message);

      conversation.addMessage(msg);

      Datastore ds = dbPersistence.getDatastore();

      Query<Conversation> query = ds.createQuery(Conversation.class).field("_id").equal(conversation.getConversationId());
      UpdateOperations<Conversation> updateOperations =  ds.createUpdateOperations(Conversation.class).add("messages", msg);

      ds.update(query, updateOperations);
//      dbPersistence.getDatastore().update(ds.createQuery(Conversation.class))
//      mongo.update(mongo.createQuery(BatchData.class)
//            .filter("uuid",theBatch.uuid)
//            .filter("fileObjects.fileName","theFileName"),updateOperations);
//      dbPersistence.getDatastore().update()

   }
}

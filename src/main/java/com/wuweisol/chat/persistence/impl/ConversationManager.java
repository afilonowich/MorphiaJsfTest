package com.wuweisol.chat.persistence.impl;

import com.google.code.morphia.query.Query;
import com.wuweisol.chat.model.Conversation;
import com.wuweisol.chat.model.Message;
import com.wuweisol.chat.persistence.ConversationDAO;
import com.wuweisol.chat.persistence.DbPersistence;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 */
@Named
public class ConversationManager
{
   @Inject
   DbPersistence dbPersistence;

   ConversationDAO dao;

   public ConversationManager()
   {
System.out.println("Conversation Manager being Created");
   }

   @PostConstruct
   public void initialize()
   {
System.out.println("Conversation Manager Initializing post construct");
      if(dbPersistence != null)
      {
         dbPersistence.getMorphia().map(Conversation.class);
         dbPersistence.getMorphia().map(Message.class);

         dao = new ConversationDAO(dbPersistence);
System.out.println("Conversation Manager Initialized");
      }
      else
         System.out.println("Null DB Persistence");
   }

   public ConversationDAO getDao()
   {
      return dao;
   }


}

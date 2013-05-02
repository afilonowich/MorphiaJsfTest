package com.wuweisol.chat.model;

//import javax.persistence.Entity;
//import javax.persistence.Id;
import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.Collection;

/**
*/
@Entity
public class Conversation
{
   @Id
   private String conversationId;

   private String event;

   private Collection<Message> messages;

   @Embedded
   private Collection<String> parties;

   public Collection<Message> getMessages()
   {
      return messages;
   }

   public void setMessages(Collection<Message> messages)
   {
      this.messages = messages;
   }

   public void addMessage(Message message)
   {
      if(messages == null)
      {
         messages = new ArrayList<Message>();
      }
      messages.add(message);
   }

   public String getConversationId()
   {
      return conversationId;
   }

   public void setConversationId(String conversationId)
   {
      this.conversationId = conversationId;
   }

   public String getEvent()
   {
      return event;
   }

   public void setEvent(String event)
   {
      this.event = event;
   }

   //   @OneToMany
   public Collection<String> getPartyIds()
   {
      return parties;
   }

   public void setPartyIds(Collection<String> parties)
   {
      this.parties = parties;
   }

   @Override
   public String toString()
   {
      return "Conversation{" +
            "conversationId='" + conversationId + '\'' +
            ", between parties=" + parties +
            '}';
   }
}

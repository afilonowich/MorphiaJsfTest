package com.wuweisol.chat.jsf.control;

import com.wuweisol.chat.model.Conversation;
import com.wuweisol.chat.model.Message;
import com.wuweisol.chat.persistence.impl.ConversationManager;
import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 */
@Named(value = "chatterBean")
//@SessionScoped
@ApplicationScoped
public class ChatterBean implements Serializable
{
   // To turn on websocket usage in JBoss, had to change the standalone.xml file set:
   // <subsystem xmlns="urn:jboss:domain:web:1.1" default-virtual-server="default-host" native="false">
   // to
   // <subsystem xmlns="urn:jboss:domain:web:1.1" default-virtual-server="default-host" native="true">
   private static final String PUSH_CHANNEL = "/gmdc_chat";

   @Inject
   ConversationManager conversationManager;

   private PushContext pushContext = PushContextFactory.getDefault().getPushContext();

   private Conversation currentConversation = null;

   private String event = "Grand Event";

   private String selectedChatPartner;

   private String chatter1 = "Aaron";
   private String message1;

   private String chatter2;
   private String message2;

   private String fullMessage;

   private Conversation conversation;

//   @Inject
   public static int counter = 0;

   public ChatterBean()
   {
System.out.println("Chatter Bean " + ++ counter  + " being created");
   }

   public void changeEvent()
   {
System.out.println("Change event set to " + event);
   }

   public String getEvent()
   {
      return event;
   }

   public void setEvent(String event)
   {
System.out.println("Setting Event " + event);
      this.event = event;
   }

   public String getChatter1()
   {
      return chatter1;
   }

   public void setChatter1(String chatter1)
   {
System.out.println("Setting chatter1 = [" + chatter1 + "]");
      this.chatter1 = chatter1;

      if(! chatter1.isEmpty() && ! chatter2.isEmpty())
      {
         loadConversation();
      }
   }

   public String getSelectedChatPartner()
   {
      return selectedChatPartner;
   }

   public void setSelectedChatPartner(String selectedChatPartner)
   {
System.out.println("Setting Selected Chat Partner " + selectedChatPartner);

      this.selectedChatPartner = selectedChatPartner;
   }

   public String getMessage1()
   {
      return message1;
   }

   public void setMessage1(String message1)
   {
System.out.println("message1 = [" + message1 + "]");
      this.message1 = message1;

      if(currentConversation != null)
      {
         conversationManager.getDao().updateConversation(currentConversation, chatter1, message1);
         fullMessage += message1;

         this.message1 = "";
      }
   }

   public String getChatter2()
   {
      return chatter2;
   }

   public void setChatter2(String chatter2)
   {
System.out.println("Setting chatter2 = [" + chatter2 + "]");
      this.chatter2 = chatter2;

      if(! chatter1.isEmpty() && ! chatter2.isEmpty())
      {
         loadConversation();
      }
   }

   public String getMessage2()
   {
      return message2;
   }

   public void setMessage2(String message2)
   {
System.out.println("message2 = [" + message2 + "]");
      this.message2 = message2;
   }

   public String getFullMessage()
   {
      return fullMessage;
   }

   public void submitSimple()
   {
System.out.println("Submit Simple selected with " + event + " and user " + chatter1);
   }

   // GUI Events
   public void submit1()
   {
System.out.println("Submit Button 1");
      // Attempt to load a message document with the chatters.
      if(message1.trim().length() > 0)
      {
         fullMessage += chatter1 + ": " + message1 + "\n";
         pushContext.push(PUSH_CHANNEL, fullMessage);
      }
   }

   // GUI Events
   public void submit2()
   {
      System.out.println("Submit Button 2");
      if(message2.trim().length() > 0)
      {
         if(conversation == null)
            createConversation();

         fullMessage += chatter2 + ": " + message2 + "\n";
         pushContext.push(PUSH_CHANNEL, fullMessage);
      }
   }

   // GUI Events
      /*
   public void lookupEvent()
   {
      Conversation conversation = findConversation(event, chatter1, chatter2);
System.out.println("Look up the event by event '" + event + "' and chatters " + chatter1 + "', '" + chatter2 + "'");
      if(chatManager == null)
         chatManager = new ChatManager();

      Conversation conversation = findConversation(event, chatter1, chatter2);
      if(conversation != null)
      {
System.out.println("   Found the conversation " + conversation);
      }
      else
System.out.println("   Did not find any matching conversation");

      if(chatManager == null)
System.out.println("   Chat Manager is null");
else
System.out.println("   Chat Manager is NOT null yeah");
   }
*/

   public List<String> getConversationList()
   {
      List<Conversation> conversations = conversationManager.getDao().getConversations(event, chatter1);
      List<String> retPartyList = new ArrayList<String>(conversations.size());

      for(Conversation conversation : conversations)
      {
         Collection<String> chatterList = conversation.getPartyIds();
         String chatterString = "";
         for(String chatter : chatterList)
         {
            if(! chatter.equals(chatter1))
            {
               if(chatterString.isEmpty())
                  chatterString = chatter;
               else
                  chatterString += ", " + chatter;
            }
         }
         if(! chatterString.isEmpty())
            retPartyList.add(chatterString);
      }

      return retPartyList;
   }

   public void loadConversation()
   {
      Conversation conversation = conversationManager.getDao().getConversation(event, chatter1, chatter2);
System.out.println("New conversation loaded of " + conversation);

      if(conversation == null)
      {
         conversation = createConversation();
      }
      if(conversation != null)
      {
         Collection<Message> messages =  conversation.getMessages();
         if(messages != null && messages.size() > 0)
         {
            fullMessage = "";
            pushContext.push(PUSH_CHANNEL, "CLEAR");
            for(Message message : messages)
            {
               String msg = message.getPartyPoster() + ": " + message.getMessage() + "\n";
               fullMessage += msg;
//               pushContext.push(PUSH_CHANNEL, msg);
            }
            pushContext.push(PUSH_CHANNEL, fullMessage);
         }
      }

      currentConversation = conversation;
   }

   /*
   public Conversation findConversation(String event, String chatter1, String chatter2)
   {
      Conversation conversation = conversationManager.getDao().getConversation(event, chatter1, chatter2);

      return conversation;
   }
   */

   public Conversation createConversation()
   {
      conversation = new Conversation();
      conversation.setEvent(event);

      List<String> partyIds = new ArrayList<String>();
      partyIds.add(chatter1);
      partyIds.add(chatter2);
      conversation.setPartyIds(partyIds);

      conversationManager.getDao().save(conversation);

      return conversation;
   }
}

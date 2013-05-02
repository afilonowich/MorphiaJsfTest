package com.wuweisol.chat.model;



//import javax.persistence.Entity;
//import javax.persistence.Id;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

import java.net.URL;
import java.util.Collection;
import java.util.Date;

/**
 */
@Entity
public class Message
{
//   @Id
//   @GeneratedValue(strategy = GenerationType.TABLE, generator = "message")
   @Id
   private String messageId;
   private String partyPoster;
   private String message;
   private Collection<URL> url;
   private Collection<URL> images;
   private Date date;

   public Message()
   {
      date = new Date();
   }

   public String getMessageId()
   {
      return messageId;
   }

   public void setMessageId(String messageId)
   {
      this.messageId = messageId;
   }

   public String getPartyPoster()
   {
      return partyPoster;
   }

   public void setPartyPoster(String partyPoster)
   {
      this.partyPoster = partyPoster;
   }

   public String getMessage()
   {
      return message;
   }

   public void setMessage(String message)
   {
      this.message = message;
   }

   public Collection<URL> getUrl()
   {
      return url;
   }

   public void setUrl(Collection<URL> url)
   {
      this.url = url;
   }

   public Collection<URL> getImages()
   {
      return images;
   }

   public void setImages(Collection<URL> images)
   {
      this.images = images;
   }

   public Date getDate()
   {
      return date;
   }

   public void setDate(Date date)
   {
      this.date = date;
   }

   @Override
   public String toString()
   {
      return "Message{" +
            "messageId='" + messageId + '\'' +
            ", partyPoster='" + partyPoster + '\'' +
            ", message='" + message + '\'' +
            ", url=" + url +
            ", images=" + images +
            ", date=" + date +
            '}';
   }
}

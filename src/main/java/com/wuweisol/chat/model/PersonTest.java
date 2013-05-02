package com.wuweisol.chat.model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;


/**
 * Created with IntelliJ IDEA.
 * User: Aaron
 * Date: 4/28/13
 * Time: 12:33 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class PersonTest
{
   @Id
   String personId;

   public PersonTest()
   {

   }
}

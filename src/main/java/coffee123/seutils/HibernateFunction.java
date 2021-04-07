/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coffee123.seutils;
 
package com;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
public class HibernateFunction {
    
    //hibernate method to delete an index
    //still a work in progress
   public static void delete(){
   try {
         tx = session.beginTransaction();
         Index index = (Index)session.get(SEindex.class,indexID); 
         session.delete(index); 
         tx.commit();
      } catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      } finally {
         session.close(); 
      }
   }
    }
}
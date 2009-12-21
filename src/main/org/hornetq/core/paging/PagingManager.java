/*
 * Copyright 2009 Red Hat, Inc.
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.hornetq.core.paging;

import org.hornetq.SimpleString;
import org.hornetq.core.journal.SequentialFile;
import org.hornetq.core.postoffice.PostOffice;
import org.hornetq.core.server.HornetQComponent;

/**
 * 
 * 
<PRE>

+------------+      1  +-------------+       N +------------+       N +-------+       1 +----------------+
| {@link PostOffice} |-------&gt; |PagingManager|-------&gt; |{@link PagingStore} | ------&gt; | {@link Page}  | ------&gt; | {@link SequentialFile} |
+------------+         +-------------+         +------------+         +-------+         +----------------+
                              |                       1 ^
                              |                         |
                              |                         |
                              |                         | 1
                              |        N +---------+
                              +--------&gt; | Address |
                                         +---------+   

</PRE>

 * 
 * @author <a href="mailto:clebert.suconic@jboss.com">Clebert Suconic</a>
 * @author <a href="mailto:tim.fox@jboss.com">Tim Fox</a>
 * @author <a href="mailto:andy.taylor@jboss.org>Andy Taylor</a>
 *
 */
public interface PagingManager extends HornetQComponent
{
   /** To return the PageStore associated with the address */
   PagingStore getPageStore(SimpleString address) throws Exception;

   /** An injection point for the PostOffice to inject itself */
   void setPostOffice(PostOffice postOffice);

   /** Used to start depaging every paged address, after a reload/restart */
   void resumeDepages() throws Exception;

   /**
    * Point to inform/restoring Transactions used when the messages were added into paging
    * */
   void addTransaction(PageTransactionInfo pageTransaction);

   /**
    * Point to inform/restoring Transactions used when the messages were added into paging
    * */
   PageTransactionInfo getTransaction(long transactionID);

   /**
    * @param transactionID
    */
   void removeTransaction(long transactionID);

   long getTotalMemory();

   /**
    * @param size
    */
   long addSize(long size);

   /**
    * Reload previously created PagingStores into memory
    * @throws Exception 
    */
   void reloadStores() throws Exception;

   SimpleString[] getStoreNames();

   void deletePageStore(SimpleString storeName) throws Exception;
}

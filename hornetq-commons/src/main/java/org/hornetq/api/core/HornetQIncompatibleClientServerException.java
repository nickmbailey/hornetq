/*
 * Copyright 2005-2014 Red Hat, Inc.
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

package org.hornetq.api.core;

import static org.hornetq.api.core.HornetQExceptionType.INCOMPATIBLE_CLIENT_SERVER_VERSIONS;

/**
 * The server version and the client version are incompatible.
 * <p>
 * Normally this means you are trying to use a newer client on an older server.
 * @author <a href="mailto:andy.taylor@jboss.org">Andy Taylor</a>
 */
public final class HornetQIncompatibleClientServerException extends HornetQException
{
   private static final long serialVersionUID = -1662999230291452298L;

   public HornetQIncompatibleClientServerException()
   {
      super(INCOMPATIBLE_CLIENT_SERVER_VERSIONS);
   }

   public HornetQIncompatibleClientServerException(String msg)
   {
      super(INCOMPATIBLE_CLIENT_SERVER_VERSIONS, msg);
   }
}

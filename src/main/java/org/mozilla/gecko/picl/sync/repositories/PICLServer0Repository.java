/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.gecko.picl.sync.repositories;

import org.mozilla.gecko.sync.repositories.Repository;
import org.mozilla.gecko.sync.repositories.delegates.RepositorySessionCreationDelegate;

import android.content.Context;

/**
 * A PICL0ServerRepository implements fetching and storing against the PICL Server ProtocolZero.
 * https://wiki.mozilla.org/Identity/AttachedServices/StorageProtocolZero
 */
public class PICLServer0Repository extends Repository {

  public final String serverURI;
  public final String userid;
  public final String collection;

  public PICLServer0Repository(String serverURI, String userid, String collection) {
    this.serverURI  = serverURI;
    this.userid = userid;
    this.collection = collection;
    //    this.collectionPath = this.serverURI + "/" + this.userid + "/storage/" + this.collection;
    //    this.collectionPathURI = new URI(this.collectionPath);
  }

  @Override
  public void createSession(RepositorySessionCreationDelegate delegate, Context context) {
    delegate.onSessionCreated(new PICLServer0RepositorySession(this));
  }
}

package org.mozilla.android.sync.synchronizer;

import org.mozilla.android.sync.SyncException;
import org.mozilla.android.sync.repositories.RepositorySession;

/**
 * An exception class that indicates that a session was passed
 * to a begin callback and wasn't expected.
 *
 * This shouldn't occur.
 *
 * @author rnewman
 *
 */
public class UnexpectedSessionException extends SyncException {
  private static final long serialVersionUID = 949010933527484721L;
  public RepositorySession session;

  public UnexpectedSessionException(RepositorySession session) {
    this.session = session;
  }
}

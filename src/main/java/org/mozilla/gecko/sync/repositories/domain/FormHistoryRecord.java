/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.gecko.sync.repositories.domain;

import org.mozilla.gecko.db.BrowserContract;
import org.mozilla.gecko.sync.ExtendedJSONObject;
import org.mozilla.gecko.sync.Logger;
import org.mozilla.gecko.sync.Utils;
import org.mozilla.gecko.sync.repositories.android.RepoUtils;

/**
 * A FormHistoryRecord represents a saved form element.
 *
 * I map a <code>fieldName</code> string to a <code>value</code> string and maintain additional Fennec data.
 *
 * {@link http://mxr.mozilla.org/services-central/source/services-central/services/sync/modules/engines/forms.js}
 */
public class FormHistoryRecord extends Record {
  private static final String LOG_TAG = "FormHistoryRecord";

  public static final String COLLECTION_NAME = "formhistory";

  public static FormHistoryRecord withIdFieldNameAndValue(long id, String fieldName, String value) {
    FormHistoryRecord fr = new FormHistoryRecord();
    fr.id  = id;
    fr.fieldName = fieldName;
    fr.fieldValue = value;

    return fr;
  }
  public long id;

  /**
   * The name of the saved form field.
   */
  public String fieldName;

  /**
   * The value of the saved form field.
   */
  public String fieldValue;

  public long fennecTimesUsed = 0;  // Integer count.
  public long fennecFirstUsed = -1; // Timestamp in microseconds.
  public long fennecLastUsed  = -1; // Timestamp in microseconds.

  public FormHistoryRecord(String guid, String collection, long lastModified, boolean deleted) {
    super(guid, collection, lastModified, deleted);
  }

  public FormHistoryRecord(String guid, String collection, long lastModified) {
    super(guid, collection, lastModified, false);
  }

  public FormHistoryRecord(String guid, String collection) {
    super(guid, collection, 0, false);
  }

  public FormHistoryRecord(String guid) {
    super(guid, COLLECTION_NAME, 0, false);
  }

  public FormHistoryRecord() {
    super(Utils.generateGuid(), COLLECTION_NAME, 0, false);
  }

  @Override
  public Record copyWithIDs(String guid, long androidID) {
    FormHistoryRecord out = new FormHistoryRecord(guid, this.collection, this.lastModified, this.deleted);
    out.androidID = androidID;
    out.sortIndex = this.sortIndex;

    // Copy FormHistoryRecord fields.
    out.id    = this.id;
    out.fieldName = this.fieldName;
    out.fieldValue = this.fieldValue;
    out.fennecTimesUsed = this.fennecTimesUsed;
    out.fennecFirstUsed = this.fennecFirstUsed;
    out.fennecLastUsed = this.fennecLastUsed;

    return out;
  }

  @Override
  public void populatePayload(ExtendedJSONObject payload) {
    putPayload(payload, "fieldName", this.fieldName);
    putPayload(payload, "value",     this.fieldValue);
  }

  @Override
  public void initFromPayload(ExtendedJSONObject payload) {
    this.fieldName  = (String) payload.get(BrowserContract.FormHistory.FIELD_NAME);
    this.fieldValue = (String) payload.get(BrowserContract.FormHistory.VALUE);
  }

  /**
   * We consider two form history records to be congruent if they represent the
   * same form element regardless of times used.
   */
  @Override
  public boolean congruentWith(Object o) {
    if (o == null || !(o instanceof FormHistoryRecord)) {
      return false;
    }
    FormHistoryRecord other = (FormHistoryRecord) o;
    if (!super.congruentWith(other)) {
      return false;
    }
    return RepoUtils.stringsEqual(this.fieldName, other.fieldName) &&
           RepoUtils.stringsEqual(this.fieldValue, other.fieldValue);
  }

  @Override
  public boolean equalPayloads(Object o) {
    if (o == null || !(o instanceof FormHistoryRecord)) {
      Logger.debug(LOG_TAG, "Not a FormHistoryRecord: " + o);
      return false;
    }
    FormHistoryRecord other = (FormHistoryRecord) o;
    if (!super.equalPayloads(other)) {
      Logger.debug(LOG_TAG, "super.equalPayloads returned false.");
      return false;
    }
    return (this.id == other.id) &&
        RepoUtils.stringsEqual(this.fieldName, other.fieldName) &&
        RepoUtils.stringsEqual(this.fieldValue, other.fieldValue) &&
        checkFennecDataAreEqual(other);
  }

  private boolean checkFennecDataAreEqual(FormHistoryRecord other) {
    return (this.id == other.id) &&
           (this.fennecTimesUsed == other.fennecTimesUsed) &&
           (this.fennecFirstUsed == other.fennecFirstUsed) &&
           (this.fennecLastUsed  == other.fennecLastUsed);
  }
}

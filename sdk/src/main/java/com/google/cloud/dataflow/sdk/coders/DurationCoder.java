/*
 * Copyright (C) 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.cloud.dataflow.sdk.coders;

import com.google.cloud.dataflow.sdk.util.common.ElementByteSizeObserver;

import com.fasterxml.jackson.annotation.JsonCreator;

import org.joda.time.Duration;
import org.joda.time.ReadableDuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A {@link Coder} for a joda {@link Duration}.
 */
public class DurationCoder extends AtomicCoder<ReadableDuration> {

  private static final long serialVersionUID = 0L;

  @JsonCreator
  public static DurationCoder of() {
    return INSTANCE;
  }

  /////////////////////////////////////////////////////////////////////////////

  private static final DurationCoder INSTANCE = new DurationCoder();

  private final Coder<Long> longCoder = VarLongCoder.of();

  private DurationCoder() {}

  private Long toLong(ReadableDuration value) {
    return value.getMillis();
  }

  private ReadableDuration fromLong(Long decoded) {
    return Duration.millis(decoded);
  }

  @Override
  public void encode(ReadableDuration value, OutputStream outStream, Context context)
      throws CoderException, IOException {
    longCoder.encode(toLong(value), outStream, context);
  }

  @Override
  public ReadableDuration decode(InputStream inStream, Context context)
      throws CoderException, IOException {
      return fromLong(longCoder.decode(inStream, context));
  }

  @Override
  public boolean consistentWithEquals() {
    return true;
  }

  @Override
  public boolean isRegisterByteSizeObserverCheap(ReadableDuration value, Context context) {
    return longCoder.isRegisterByteSizeObserverCheap(toLong(value), context);
  }

  @Override
  public void registerByteSizeObserver(
      ReadableDuration value, ElementByteSizeObserver observer, Context context) throws Exception {
    longCoder.registerByteSizeObserver(toLong(value), observer, context);
  }
}

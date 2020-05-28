/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.exoplayer2.source.chunk;

import androidx.annotation.Nullable;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import java.io.IOException;

/**
 * Extracts samples and track {@link Format Formats} from chunks.
 *
 * <p>The {@link TrackOutputProvider} passed to {@link #init} provides the {@link TrackOutput
 * TrackOutputs} that receive the extracted data.
 */
public interface ChunkExtractor {

  /** Provides {@link TrackOutput} instances to be written to during extraction. */
  interface TrackOutputProvider {

    /**
     * Called to get the {@link TrackOutput} for a specific track.
     *
     * <p>The same {@link TrackOutput} is returned if multiple calls are made with the same {@code
     * id}.
     *
     * @param id A track identifier.
     * @param type The type of the track. Typically one of the {@link C} {@code TRACK_TYPE_*}
     *     constants.
     * @return The {@link TrackOutput} for the given track identifier.
     */
    TrackOutput track(int id, int type);
  }

  /**
   * Returns the {@link SeekMap} most recently output by the extractor, or null if the extractor has
   * not output a {@link SeekMap}.
   */
  @Nullable
  SeekMap getSeekMap();

  /**
   * Returns the sample {@link Format}s for the tracks identified by the extractor, or null if the
   * extractor has not finished identifying tracks.
   */
  @Nullable
  Format[] getSampleFormats();

  /**
   * Initializes the wrapper to output to {@link TrackOutput}s provided by the specified {@link
   * TrackOutputProvider}, and configures the extractor to receive data from a new chunk.
   *
   * @param trackOutputProvider The provider of {@link TrackOutput}s that will receive sample data.
   * @param startTimeUs The start position in the new chunk, or {@link C#TIME_UNSET} to output
   *     samples from the start of the chunk.
   * @param endTimeUs The end position in the new chunk, or {@link C#TIME_UNSET} to output samples
   *     to the end of the chunk.
   */
  void init(@Nullable TrackOutputProvider trackOutputProvider, long startTimeUs, long endTimeUs);

  /**
   * Reads from the given {@link ExtractorInput}.
   *
   * @param input The input to read from.
   * @return One of the {@link Extractor}{@code .RESULT_*} values.
   * @throws IOException If an error occurred reading from or parsing the input.
   */
  int read(ExtractorInput input) throws IOException;
}

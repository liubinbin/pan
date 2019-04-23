/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.liubinbin.experiment.byteop;

import java.nio.ByteBuffer;

/**
 * some method directly use the HBase's ByteBufferUtils.
 * If we need totally decouple the CCSMap with HBase, we need implement it.
 * All method don't change the ByteBuffer's position.
 */
public final class ByteBufferUtils {

    private ByteBufferUtils() {
    }

    /**
     * Copy from one buffer to another from given offset. This will be absolute positional copying and
     * won't affect the position of any of the buffers.
     *
     * @param in                source buffer
     * @param out               destination buffer
     * @param sourceOffset      offset in the source buffer
     * @param destinationOffset offset in the destination buffer
     * @param length            how many bytes to copy
     */

    public static boolean compareAndSetLong(ByteBuffer byteBuffer, int offset, long expected,
                                            long update) {
        return UnsafeHelp.compareAndSetLong(byteBuffer, offset, expected, update);
    }

    public static boolean compareAndSetInt(ByteBuffer byteBuffer, int offset, int expected,
                                           int update) {
        return UnsafeHelp.compareAndSetInt(byteBuffer, offset, expected, update);
    }


}
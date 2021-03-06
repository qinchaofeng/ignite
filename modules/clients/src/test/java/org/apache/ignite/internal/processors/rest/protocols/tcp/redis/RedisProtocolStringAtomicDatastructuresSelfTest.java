/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.internal.processors.rest.protocols.tcp.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import redis.clients.jedis.Jedis;

/**
 * Tests for String atomic datastructures commands of Redis protocol.
 */
@RunWith(JUnit4.class)
public class RedisProtocolStringAtomicDatastructuresSelfTest extends RedisCommonAbstractTest {
    /**
     * Test that threads with datastructures commands wasn't deadlocked when PME happens.
     *
     * @throws Exception If failed.
     */
    @Test
    public void testAtomicCommandsTopologyChange() throws Exception {
        try (Jedis jedis = pool.getResource()) {
            int size = grid(0).cachesx().size();

            jedis.incr("key1");

            // Expect that datastructures cache was created and init PME.
            assertTrue("Topology wasn't changed.", grid(0).cachesx().size() > size);

            for (int i = 0; i < 1000; i++)
                jedis.get("nonExistKey");
        }
    }
}

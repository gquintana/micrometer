/**
 * Copyright 2017 Pivotal Software, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micrometer.core.instrument.binder.jvm;

import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.lang.NonNullApi;
import io.micrometer.core.lang.NonNullFields;

import java.lang.management.CompilationMXBean;
import java.lang.management.ManagementFactory;

import static java.util.Collections.emptyList;

@NonNullApi
@NonNullFields
public class CompilationMetrics implements MeterBinder {
    private final Iterable<Tag> tags;

    public CompilationMetrics() {
        this(emptyList());
    }

    public CompilationMetrics(Iterable<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        CompilationMXBean compilationBean = ManagementFactory.getCompilationMXBean();
        if (compilationBean.isCompilationTimeMonitoringSupported()) {
            FunctionCounter.builder("jvm.compilation.time.total", compilationBean, CompilationMXBean::getTotalCompilationTime)
                    .tags(Tags.concat(tags, "compiler", compilationBean.getName()))
                    .description("The approximate accumulated elapsed time spent in compilation")
                    .baseUnit("ms")
                    .register(registry);
        }
    }
}

package org.softauto.jaxrs.service;

import com.google.common.base.Preconditions;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class TestDefinition {
    TestDescriptor testDescriptor;
    Map<String, StepDefinition> steps;
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(TestDefinition.class);

    public static Builder builder(TestDescriptor testDescriptor) {
        return new Builder(testDescriptor);
    }

    private TestDefinition(TestDescriptor testDescriptor, Map<String, StepDefinition> steps) {
        this.testDescriptor = (TestDescriptor) Preconditions.checkNotNull(testDescriptor, "testDescriptor");
        this.steps = Collections.unmodifiableMap(new HashMap(steps));
    }

    public Collection<StepDefinition> getSteps() {
        return this.steps.values();
    }


    public StepDefinition getStep(String stepName) {
        return (StepDefinition)this.steps.get(stepName);
    }




    public static final class Builder {
        //private final String serviceName;
        private final TestDescriptor testDescriptor;
        private final Map<String, StepDefinition> steps;

        private Builder(String serviceName) {
            this.steps = new HashMap();
            this.testDescriptor = null;
        }

        private Builder(TestDescriptor testDescriptor) {
            this.steps = new HashMap();
            this.testDescriptor = (TestDescriptor)Preconditions.checkNotNull(testDescriptor, "serviceDescriptor");

        }

        public Builder addStep(IStepDescriptor step, CallerHandler handler) {
            return this.addStep(StepDefinition.create((IStepDescriptor)Preconditions.checkNotNull(step, "method must not be null"), (CallerHandler)Preconditions.checkNotNull(handler, "handler must not be null")));
        }

        public   Builder addStep(StepDefinition def) {
            IStepDescriptor step = def.getStepDescriptor();
            String name = step.getFullMethodName();
            Preconditions.checkState(!this.steps.containsKey(name), "step by same name already registered: %s", name);
            this.steps.put(name, def);
            return this;
        }

        public TestDefinition build() {
            Map<String, StepDefinition> tmpSteps = new HashMap(this.steps);

            ConcurrentMap<String, IStepDescriptor> entrys = testDescriptor.getSteps();
            Set<Map.Entry<String, IStepDescriptor>> entrySet = entrys.entrySet();
            Iterator<Map.Entry<String, IStepDescriptor>> iterator = entrySet.iterator();

            StepDefinition removed;
            IStepDescriptor descriptorStep;
            do {
                if (!iterator.hasNext()) {
                    if (tmpSteps.size() > 0) {
                        throw new IllegalStateException("No entry in descriptor matching bound method " + ((StepDefinition)tmpSteps.values().iterator().next()).getStepDescriptor().getFullMethodName());
                    }

                    return new TestDefinition(testDescriptor, this.steps);
                }

                descriptorStep = (IStepDescriptor)iterator.next().getValue();
                removed = (StepDefinition)tmpSteps.remove(descriptorStep.getFullMethodName());
                if (removed == null) {
                    throw new IllegalStateException("No step bound for descriptor entry " + descriptorStep.getFullMethodName());
                }
            } while(removed.getStepDescriptor() == descriptorStep);

            throw new IllegalStateException("Bound step for " + descriptorStep.getFullMethodName() + " not same instance as method in service descriptor");
        }
    }
}

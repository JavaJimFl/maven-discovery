package org.kaib.discover.maven.process;

public class MavenDependencySearchPipeline<I, O>{
	
    private final Step<I, O> currentStep;
    
    public MavenDependencySearchPipeline(Step<I, O> currentStep) {
    	
        this.currentStep = currentStep;
    }

    private <NxtO> MavenDependencySearchPipeline<I, NxtO> pipe(Step<O, NxtO> next) {
    	
        return new MavenDependencySearchPipeline<>(input -> next.process(currentStep.process(input)));
    }


    public O execute(I input) throws Step.StepException {
    	
        return currentStep.process(input);
    }

}

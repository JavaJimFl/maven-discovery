package org.kaib.discover.maven.process;

public interface Step<I, O> {
	
	public static class StepException extends Exception {
		private static final long serialVersionUID = 1L;

		public StepException(String message) {
			super(message);
		}
	}
	
	public O process(I input);
}

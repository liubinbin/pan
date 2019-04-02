package cn.liubinbin.experiment.groupbyer;

/**
 *
 * @author liubinbin
 *
 */
public class LevelTooDeepException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LevelTooDeepException() {
		super();
	}

	public LevelTooDeepException(final String message) {
		super(message);
	}

	public LevelTooDeepException(final String message, final Throwable t) {
		super(message, t);
	}

	public LevelTooDeepException(final Throwable t) {
		super(t);
	}

}

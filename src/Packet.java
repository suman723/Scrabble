/**
 * Wrapper class for messages from the command panel. Maybe useful in other areas later. Simply allows the command panel to return not just
 * the data requested but an error if the user entered data that wasn't a correct choice such as a country they can't reinforce or invading with
 * more troops than they have.
 * @param <T> For now Country and Integer are probably the two most likely choices but generic means it should be easily extendable.
 *           Note: if using a custom class please ensure it has a useful toString method.
 *
 * Team DasKlos. Student Numbers: Jerry Klos 19200313, Suman Shil- 18760791, Simon Farrelly 19739425
 */

public class Packet<T>
{
	// Type of response
	public enum Type {SUCCESS, ERROR;}
	private final Type type;

	// User selection or null if an error
	private final T data;
	// Optional information string explaining error (or other future types)
	private final String message;
	// Stores the exact string user entered
	private final String userInput;

	/**
	 * Constructor
	 *
	 * @param data data caller is expecting
	 * @param type return type success/failure
	 * @param message optional message. Usually explaining reason for failure to get data
	 */
	public Packet(T data, Type type, String message, String userInput)
	{
		this.data = data;
		this.type = type;
		this.message = message;
		this.userInput = userInput;
	}

	// Same as above but black message
	public Packet(T data, Type type, String userInput)
	{
		this(data, type, "", userInput);
	}

	public T getData()
	{
		return data;
	}

	public Type getType()
	{
		return type;
	}

	public String getMessage()
	{
		return message;
	}

	public String getUserInput()
	{
		return userInput;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Data: ");
		sb.append( (data != null) ? data : "null");
		sb.append(" Type: ");
		sb.append(type);
		sb.append(" Message: ");
		sb.append(message);
		return sb.toString();
	}
}

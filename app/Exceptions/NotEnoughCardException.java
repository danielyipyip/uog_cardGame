package Exceptions;

public class NotEnoughCardException extends IllegalArgumentException{
	public NotEnoughCardException(String message) {
		super(message);
	}
}

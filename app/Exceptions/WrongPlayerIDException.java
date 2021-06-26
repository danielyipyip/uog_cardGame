package Exceptions;

public class WrongPlayerIDException extends IllegalArgumentException{
	public WrongPlayerIDException(String message) {
		super(message);
	}
}

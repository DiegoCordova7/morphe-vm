package dev.eidos.vm.core;

import dev.eidos.vm.exception.stack.*;

/**
	* Represents a stack used by the VM to manage heap indices.
	* <p>
	* VMStack provides typical stack operations such as push, pop, peek, and size checks.
	* It is implemented using an integer array, where each element represents an index
	* in the VMHeap. Overflow and underflow conditions throw {@link VMStackException}.
	* </p>
	*/
public final class VMStack {

	private final int[] stack;
	private int top = -1;

	/**
		* Creates a new VMStack with the specified capacity.
		*
		* @param capacity Maximum number of elements the stack can hold.
		*/
	public VMStack(int capacity) {
		stack = new int[capacity];
	}

	/**
		* Retrieves the element at a given index in the stack.
		*
		* @param index Index of the element (0-based, from bottom of stack).
		* @return The heap index stored at the given stack position.
		* @throws InvalidStackIndexException If the index is invalid or out of range.
		*/
	public int get(int index) {
		if (index < 0 || index > top) throw new InvalidStackIndexException(index, size());
		return stack[index];
	}

	/**
		* Pushes a heap index onto the top of the stack.
		*
		* @param heapIndex The index in VMHeap to push.
		* @throws StackOverflowException If the stack is full (overflow).
		*/
	public void push(int heapIndex) {
		if (top + 1 >= stack.length) throw new StackOverflowException(stack.length);
		stack[++top] = heapIndex;
	}

	/**
		* Pops and returns the top element of the stack.
		*
		* @return The heap index at the top of the stack.
		* @throws StackUnderflowException If the stack is empty (underflow).
		*/
	public int pop() {
		if (top < 0) throw new StackUnderflowException();
		return stack[top--];
	}

	/**
		* Returns the top element without removing it.
		*
		* @return The heap index at the top of the stack.
		* @throws EmptyStackException If the stack is empty.
		*/
	public int peek() {
		if (top < 0) throw new EmptyStackException();
		return stack[top];
	}

	/**
		* Checks whether the stack is empty.
		*
		* @return True if the stack has no elements, false otherwise.
		*/
	public boolean isEmpty() {
		return top == -1;
	}

	/**
		* Returns the current number of elements in the stack.
		*
		* @return The size of the stack.
		*/
	public int size() {
		return top + 1;
	}

	/**
		* Returns a string representation of the stack contents.
		*
		* @return A string listing the heap indices in the stack from bottom to top.
		*/
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i <= top; i++) {
			sb.append(stack[i]);
			if (i < top) sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}
}